package com.crts.config;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.crts.entity.UserEntity;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UserPDFExporter {

	private List<UserEntity> listUsers;

	public UserPDFExporter(List<UserEntity> listUsers) {
		this.listUsers = listUsers;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.darkGray);
		cell.setPadding(7);

		com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		/*
		 * cell.setPhrase(new Phrase("User_id", font)); table.addCell(cell);
		 */

		cell.setPhrase(new Phrase("Username", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("User_First_Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("User_Last_Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("User_email", font));
		table.addCell(cell);

		/*
		 * cell.setPhrase(new Phrase("User_Password", font)); table.addCell(cell);
		 */

		cell.setPhrase(new Phrase("Created_Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Created_By", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Is_User_Active", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
		for (UserEntity user : listUsers) {
			// table.addCell(String.valueOf(user.getuId()));
			table.addCell(user.getuName());
			table.addCell(user.getuFName());
			table.addCell(user.getuLName());
			table.addCell(user.getuEmail());
			// table.addCell(user.getuPassword());
			table.addCell(String.valueOf(user.getuCDate()));
			table.addCell(user.getuCBy());
			table.addCell(String.valueOf(user.isuIsActive()));

		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(14);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("List of Users", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 2.5f, 4.0f, 4.0f, 5.5f, 3.5f, 3.0f, 3.5f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();

	}
}