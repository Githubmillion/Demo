package com.theatre.seating.domain;

public class TicketRequest implements Comparable<TicketRequest> {
	
	public enum RequestType {
		NEW, PROCESSED;
	}
	
	private final String personName;
	private final int numberOfTicketsRequested;
	private RequestType requestStatus;
	
	public TicketRequest(String personName, int numberOfTicketsRequested) {
		super();
		this.personName = personName;
		this.numberOfTicketsRequested = numberOfTicketsRequested;
		this.requestStatus = RequestType.NEW;
	} 

	public String getPersonName() {
		return personName;
	}
	
	public int getNumberOfTicketsRequested() {
		return numberOfTicketsRequested;
	}

	public void setRequestCompleted() {
		this.requestStatus = RequestType.PROCESSED;
	}
	
	public RequestType getRequestStatus() {
		return requestStatus;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numberOfTicketsRequested;
		result = prime * result + ((personName == null) ? 0 : personName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TicketRequest other = (TicketRequest) obj;
		if (numberOfTicketsRequested != other.numberOfTicketsRequested)
			return false;
		if (personName == null) {
			if (other.personName != null)
				return false;
		} else if (!personName.equals(other.personName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TicketRequest [personName=" + personName
				+ ", numberOfTicketsRequested=" + numberOfTicketsRequested
				+ "]";
	}

	@Override
	public int compareTo(TicketRequest other) {
		return this.numberOfTicketsRequested-other.numberOfTicketsRequested;
	}
	
	
	
}
