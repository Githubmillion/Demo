package com.theatre.seating.alloc;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.theatre.seating.domain.Section;
import com.theatre.seating.domain.Theatre;
import com.theatre.seating.domain.TicketRequest;
import com.theatre.seating.domain.TicketRequest.RequestType;
import com.theatre.seating.domain.TicketRequestResult;
import com.theatre.seating.domain.TicketRequestResult.Status;

public class TheatreSeatAllocator {
	
	private static final Logger logger = Logger.getLogger(TheatreTicketSeller.class.getCanonicalName());
	
	public static Map<TicketRequest, TicketRequestResult> allocateSeats(List<TicketRequest> requests, Theatre theatre) {
		final Map<TicketRequest, TicketRequestResult> results = new HashMap<>(requests.size());
		try {
			for(TicketRequest request : requests) {
				
				if(RequestType.PROCESSED.equals(request.getRequestStatus())) 
					continue;
				
				int seatsRequested = request.getNumberOfTicketsRequested();
				int seatsLeftAtTheatre = theatre.getCapacity()-theatre.getOccupied();
				if(seatsRequested>seatsLeftAtTheatre) {
					TicketRequestResult result = new TicketRequestResult(request);
					result.setStatus(Status.FAIL_TO_ALLOT);
					request.setRequestCompleted();
					results.put(request, result);
				} else {
					
					List<Section> sections = theatre.getSections();
					for(Section section : sections) {
						int seatsLeftAtSection = section.getSeatsLeft();
						if(seatsRequested == seatsLeftAtSection) { // check sections inside row and see if the request can be filled
							TicketRequestResult result = prepareResult(request, section);
							results.put(request, result);
							break;
						} else if(seatsRequested<seatsLeftAtSection) { // if some seats left, try to fill with best match
							seatsLeftAtSection = section.getSeatsLeft()-request.getNumberOfTicketsRequested();
							TicketRequest matchedRequest = getBestMatchRequest(requests, seatsLeftAtSection);
							if(matchedRequest != null) {
								TicketRequestResult matchedRequestResult = prepareResult(matchedRequest, section);
								results.put(matchedRequest, matchedRequestResult);

								TicketRequestResult result = prepareResult(request, section);
								results.put(request, result);
								break;
							} else {  // if it can't find the best match for seating - see if the request can fit into any section
								
								Section found = getSuitableSection(sections, request);
								
								if(found != null){
									TicketRequestResult result = prepareResult(request, found);
									results.put(request, result);
									break;
								} else {
									TicketRequestResult result = prepareResult(request, section);
									results.put(request, result);
									break;

								}
							}
						} 
					}
						
				}
				if(RequestType.NEW.equals(request.getRequestStatus())) {
					TicketRequestResult result = new TicketRequestResult(request);
					result.setStatus(Status.CALL_TO_SPLIT);
					results.put(request, result);
				}
			} 
			
		} catch(Exception e) {
			logger.log(Level.SEVERE, "Error occured while processing the seating requests", e);
		}
		return results;
		
	}

	
	private static TicketRequest getBestMatchRequest(List<TicketRequest> requests, int seatsLeftAtSection) {
		for(TicketRequest request : requests) {
			if(RequestType.NEW.equals(request.getRequestStatus())) {
				if(request.getNumberOfTicketsRequested() == seatsLeftAtSection) {
					return request;
				}
			}
		}
		return null;
	}
	
	private static Section getSuitableSection(List<Section> allSections, TicketRequest request) {
		Section found = null;
		Collections.sort(allSections);
		Section tempSection = new Section(request.getNumberOfTicketsRequested(), 0, 0);
		int index = Collections.binarySearch(allSections, tempSection, new Comparator<Section>() {
			@Override
			public int compare(Section current, Section other) {
				return current.getSeatsLeft()-other.getSeatsLeft();
			}
		});
		
		if(index>=0) {
			found = allSections.get(index);
		}
		return found;
	}
	
	private static TicketRequestResult prepareResult(TicketRequest request, Section seatingSection) {
		TicketRequestResult result = new TicketRequestResult(request);
		result.setStatus(Status.CONFIRMED);
		result.setSection(seatingSection);
		request.setRequestCompleted();
		seatingSection.setOccupied(request.getNumberOfTicketsRequested());
		seatingSection.notifyAllObservers(result);
		return result;
	}
	
	
	
}
