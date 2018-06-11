package com.theatre.seating.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.theatre.seating.domain.TicketRequestResult.Status;

public class Theatre implements Observer {
	
	private int capacity;
	private int occupied;
	private final List<Section> sections;
	
	public Theatre() {
		this.sections = new ArrayList<>(0);
	}

	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}
	
	public int getOccupied() {
		return occupied;
	}

	public int getCapacity() {
		return capacity;
	}
	
	public void addRow(Section section) {
		if(section !=null) {
			sections.add(section);
			capacity += section.getCapacity();
		}
	}
	
	public List<Section> getSections() {
		return sections;
	}

	@Override
	public void update(TicketRequestResult result) {
		if(result != null && result.getStatus().equals(Status.CONFIRMED)) {
			TicketRequest incomingRequest = result.getRequest();
			if(incomingRequest != null) {
				occupied += incomingRequest.getNumberOfTicketsRequested();
			}
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int rowNum =0;
		for(Section section : sections) {
			if(rowNum == 0) {
				rowNum  = section.getRowNumber();
			} 
			if(rowNum != section.getRowNumber()) {
				sb.append("\n");
				rowNum = section.getRowNumber();
			}
			sb.append(section.getCapacity()).append(' ');
			
			
		}
		return sb.toString();
	}
	
}
