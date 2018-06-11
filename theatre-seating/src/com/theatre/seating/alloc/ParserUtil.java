package com.theatre.seating.alloc;

import java.util.StringTokenizer;

import com.theatre.seating.domain.Section;
import com.theatre.seating.domain.Theatre;
import com.theatre.seating.domain.TicketRequest;

public class ParserUtil {

	public static void addRows(Theatre theatre, String line, int rowNum) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(line, " ");
			int sectionCount =1;
			while(tokenizer.hasMoreTokens()) {
				String capacityStr  = tokenizer.nextToken();
				int capacity = Integer.parseInt(capacityStr);
				Section section = new Section(capacity, sectionCount++, rowNum);
				section.registerObservers(theatre);
				theatre.addRow(section);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static TicketRequest parseTicketRequest(String line) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(line, " ");
			String name = tokenizer.nextToken();
			int ticketsRequested = Integer.parseInt(tokenizer.nextToken());
			return new TicketRequest(name, ticketsRequested);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
