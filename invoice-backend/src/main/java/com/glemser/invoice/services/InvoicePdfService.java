package com.glemser.invoice.services;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.glemser.invoice.entities.Employee;
import com.glemser.invoice.entities.Invoice;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class InvoicePdfService {

    public byte[] generateInvoicePdf(Invoice invoice) {
        String html = buildInvoiceHtml(invoice);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);     // baseUri not needed for now
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }

    public String buildInvoiceHtml(Invoice invoice) {

        Employee emp = invoice.getEmployee();

        // Company (seller)
        String companyName = safe(emp.getCompanyName());
        String companyAddr = safe(emp.getCompanyAddress());
        String phone = safe(emp.getPhoneNumber());

        // Client (bill to)
        String clientName = safe(emp.getClientName());
        String clientAddr = safe(emp.getClientAddress());
        String clientEmail = safe(emp.getClientEmail());

        // Period
        String period = invoice.getPeriodCovered() == null ? "" : invoice.getPeriodCovered().toString();

        // Formatting (match screenshot: rate shows like $51, no decimals)
        double hours = invoice.getHoursWorked();
        double rate = emp.getRate();
        double amount = invoice.getAmount();

        return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8"/>
          <style>
            @page { size: Letter; margin: 36px; }
            body { font-family: Arial, sans-serif; font-size: 12px; color: #111; }

            /* ===== HEADER (PDF-safe table layout) ===== */
            .header-table { width: 100%%; border-collapse: collapse; }
            .header-left  { width: 55%%; vertical-align: top; }
            .header-right { width: 45%%; vertical-align: top; text-align: right; }

            .company-name { font-size: 18px; font-weight: 400; color: #1b2b4a; margin: 0; }
            .company-line { margin: 6px 0 0 0; font-size: 12px; }

            .invoice-title {
              font-size: 30px;
              font-weight: 800;
              letter-spacing: 0.5px;
              color: #1b2b4a;
              margin: 0 0 6px 0;
              line-height: 1;
            }

            .meta-table {
              width: 300px;
              border-collapse: collapse;
              margin-top: 8px;
              margin-left: auto;   /* push to right */
              font-size: 12px;
            }
            .meta-table th {
              background: #1b2b4a;
              color: #fff;
              padding: 6px 10px;
              border: 1px solid #1b2b4a;
              text-align: center;
              font-weight: 700;
            }
            .meta-table td {
              padding: 6px 10px;
              border: 1px solid #7b8798;
              text-align: center;
            }

            /* ===== BILL TO ===== */
            .billto-wrap { margin-top: 40px; }
            .billto-label {
              display: inline-block;
              background: #1b2b4a;
              color: #fff;
              padding: 4px 10px;
              font-weight: 800;
              letter-spacing: 1px;
              border: 2px solid #6d778a;
              width: 260px;      /* short bar like screenshot */
              font-size: 12px;
            }
            .billto {
              margin-top: 14px;
              font-size: 12px;
              line-height: 1.5;
            }

            /* ===== ITEMS TABLE ===== */
            .items {
              margin-top: 60px;
              width: 100%%;
              border-collapse: collapse;
              font-size: 12px;
            }
            .items th {
              background: #1b2b4a;
              color: #fff;
              padding: 6px 8px;         /* compact like screenshot */
              border: 1px solid #1b2b4a;
              text-align: left;
              font-weight: 700;
            }
            .items td {
              padding: 6px 8px;         /* compact like screenshot */
              border: 1px solid #000;   /* screenshot has strong lines */
              vertical-align: top;
            }
            .right { text-align: right; }
            .nowrap { white-space: nowrap; }

            /* ===== TOTAL ===== */
            .total {
              margin-top: 60px;
              font-size: 14px;
              font-weight: 800;
            }
          </style>
        </head>

        <body>

          <table class="header-table">
            <tr>
              <td class="header-left">
                <div class="company-name">%s</div>
                <div class="company-line">%s</div>
                <div class="company-line">Phone: %s</div>
              </td>

              <td class="header-right">
                <div class="invoice-title">INVOICE</div>

                <table class="meta-table">
                  <tr>
                    <th>INVOICE #</th>
                    <th>Period Covered</th>
                  </tr>
                  <tr>
                    <td>%s</td>
                    <td>%s</td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>

          <div class="billto-wrap">
            <div class="billto-label">BILL TO</div>
            <div class="billto">
              <div>%s</div>
              <div>%s</div>
              <div>%s</div>
            </div>
          </div>

          <table class="items">
            <tr>
              <th style="width:55%%;">Description</th>
              <th style="width:15%%;">Hours Worked</th>
              <th style="width:15%%;">Rate (per hour)</th>
              <th style="width:15%%;">Amount</th>
            </tr>
            <tr>
              <td>%s</td>
              <td class="right nowrap">%.2f</td>
              <td class="right nowrap">$%.0f</td>
              <td class="right nowrap">$%,.2f</td>
            </tr>
          </table>

          <div class="total">TOTAL: $%,.2f</div>

        </body>
        </html>
        """.formatted(
            companyName,
            companyAddr,
            phone,

            safe(invoice.getInvoiceNumber()),
            period,

            clientName,
            clientAddr,
            clientEmail,

            safe(invoice.getDescription()),
            hours,
            rate,
            amount,
            amount
        );
    }


    private String safe(String s) {
        return s == null ? "" : s;
    }

}
