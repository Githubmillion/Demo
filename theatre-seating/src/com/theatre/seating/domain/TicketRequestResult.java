package com.theatre.seating.domain;

public class TicketRequestResult {
	
	public enum Status {
		
		CONFIRMED,CALL_TO_SPLIT,FAIL_TO_ALLOT
		
	}

	private final TicketRequest request;
	private Status status;
	private Section section;
	
	public TicketRequestResult(TicketRequest request) {
		this.request = request;
	}
	
	public TicketRequestResult(TicketRequest request, Status status) {
		super();
		this.request = request;
		this.status = status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public TicketRequest getRequest() {
		return request;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public Section getSection() {
		return section;
	}
	
	public void setSection(Section section) {
		this.section = section;
	}
	
}
