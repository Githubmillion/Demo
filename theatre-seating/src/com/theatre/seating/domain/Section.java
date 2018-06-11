package com.theatre.seating.domain;

import java.util.ArrayList;
import java.util.List;

public class Section implements Comparable<Section>{
	
	private final int sectionNumber;
	private final int rowNumber;
	private final int capacity;
	private int occupied;
	private final List<Observer> observers;
	
	public Section(int capacity, int sectionNumber, int rowNumber) {
		super();
		this.capacity = capacity;
		this.sectionNumber = sectionNumber;
		this.rowNumber = rowNumber;
		this.observers = new ArrayList<>(0);
	}

	public void setOccupied(int occupied) {
		this.occupied += occupied;
	}
	
	public int getSeatsLeft() {
		return capacity - occupied;
	}
	
	public int getOccupied() {
		return occupied;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getSectionNumber() {
		return sectionNumber;
	}
	
	public int getRowNumber() {
		return rowNumber;
	}

	public void registerObservers(Observer... observers) {
		for(Observer observer : observers) {
			this.observers.add(observer);
		}
	}
	
	public void notifyAllObservers (TicketRequestResult result) {
		observers.forEach(observer -> {
			observer.update(result);
		});
	}

	
	@Override
	public int compareTo(Section other) {
		int occupation = this.getSeatsLeft()-other.getSeatsLeft(); // check occupation
		if(occupation !=0) {
			return occupation;
		}
		int rowComparision = this.rowNumber-other.rowNumber;
		if(rowComparision !=0) {
			return rowComparision;
		}
		return this.sectionNumber - other.sectionNumber;
	}

	@Override
	public String toString() {
		return "Section [sectionNumber=" + sectionNumber + ", rowNumber=" + rowNumber + ", capacity=" + capacity
				+ ", occupied=" + occupied + "]";
	}
	
}
