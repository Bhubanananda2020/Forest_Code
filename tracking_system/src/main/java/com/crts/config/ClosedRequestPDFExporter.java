package com.crts.config;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.crts.entity.StatusEntityview;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ClosedRequestPDFExporter {

	private List<StatusEntityview> listRequests;

	public ClosedRequestPDFExporter(List<StatusEntityview> listRequests) {
		this.listRequests = listRequests;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.GRAY);
		cell.setPadding(7);

		com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);


		cell.setPhrase(new Phrase("reqtitle", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("status_desc", font));
		table.addCell(cell);
		
		
		  cell.setPhrase(new Phrase("reqassignto", font)); table.addCell(cell);
		 

		cell.setPhrase(new Phrase("date", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("severity1", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("piority1", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("age", font));
		table.addCell(cell);
	}

	private void writeTableData(PdfPTable table) {
		for (StatusEntityview status : listRequests) {
			table.addCell(status.getReqtitle());
			table.addCell(status.getStatus_desc());
		//	table.addCell(Integer.valueOf(status.getReqassignto()));
			//System.out.println(status.getReqassignto());
			table.addCell(Integer.toString(status.getReqassignto()));
			table.addCell(String.valueOf(status.getDate()));
			table.addCell(String.valueOf(status.getSeverity1()));
			table.addCell(String.valueOf(status.getPiority1()));
			table.addCell(String.valueOf(status.getAge()));
	
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(14);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("List of Closed Requests", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 2.5f, 3.5f, 2.0f, 4.0f, 1.5f, 1.5f, 1.0f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();

	}

}
