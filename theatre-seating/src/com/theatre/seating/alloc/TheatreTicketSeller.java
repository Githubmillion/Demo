package com.theatre.seating.alloc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.theatre.seating.domain.Theatre;
import com.theatre.seating.domain.TicketRequest;
import com.theatre.seating.domain.TicketRequestResult;
import com.theatre.seating.domain.TicketRequestResult.Status;

public class TheatreTicketSeller {

	private static final Logger logger = Logger.getLogger(TheatreTicketSeller.class.getCanonicalName());
	
	public static void main(String[] args) {
			
			BufferedReader reader = null;
			Theatre theatre = new Theatre();
			List<TicketRequest> requests = new ArrayList<>();
			try {
				reader = new BufferedReader(new InputStreamReader(System.in));
				int emptyLineCounter = 0;
				int lineCounter =1;
				while (true) {
					String line = reader.readLine();
					if(line!=null && !line.isEmpty() && emptyLineCounter == 0) {
						ParserUtil.addRows(theatre, line, lineCounter++);
					} else if (line!=null && !line.isEmpty() && emptyLineCounter == 1) {
						TicketRequest request = ParserUtil.parseTicketRequest(line);
						requests.add(request);
					} else if (line!=null && line.isEmpty() && emptyLineCounter ==0) { // 
						emptyLineCounter++;
						lineCounter = 1;
						continue;
					} else if (line!=null && line.isEmpty() && emptyLineCounter == 1) { // 
						runTheatreTicketAllocation(requests, theatre);
						break;
					} 

				}


			} catch(Throwable t) {
				logger.log(Level.SEVERE, "Error occured while reading input from console", t);
			} finally {
				if(reader !=null)
					try {
						reader.close();
					} catch (IOException e) {}
			}
		
	}
	
	
	private static void runTheatreTicketAllocation(List<TicketRequest> requests, Theatre theatre) {
		
		final Map<TicketRequest, TicketRequestResult> requestResults = TheatreSeatAllocator.allocateSeats(requests, theatre);
		requests.forEach(request -> {
			TicketRequestResult result = requestResults.get(request);
			if(result.getStatus().equals(Status.CONFIRMED)) {
				System.out.println(result.getRequest().getPersonName() + " Row "+result.getSection().getRowNumber() + " Section " + result.getSection().getSectionNumber());
			} else if (result.getStatus().equals(Status.CALL_TO_SPLIT)) {
				System.out.println(result.getRequest().getPersonName() + " Call to split party.");
			} else if (result.getStatus().equals(Status.FAIL_TO_ALLOT)) {
				System.out.println(result.getRequest().getPersonName() + " Sorry, we can't handle your party.");
			} else {
				System.out.println("something went wrong");
			}
		});

	}
	
}
