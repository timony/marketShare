package cz.timony.marketshare.output;

import cz.timony.marketshare.domain.Market;
import cz.timony.marketshare.domain.Share;

import java.text.NumberFormat;
import java.util.Locale;

public class HtmlMarketExporter implements MarkerExporter {

    private static final NumberFormat UNIT_FORMAT = NumberFormat.getIntegerInstance(Locale.US);

    @Override
    public String export(Market market, String title) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\">\n")
                .append("<head>\n")
                .append("  <meta charset=\"UTF-8\">\n")
                .append("  <title>").append(escape(title)).append("</title>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("  <h2>").append(escape(title)).append("</h2>\n")
                .append("  <market border=\"1\" cellspacing=\"0\" cellpadding=\"6\">\n")
                .append("    <thead><tr><th>Vendor</th><th>Units</th><th>Share</th></tr></thead>\n")
                .append("    <tbody>\n");

        for (Share row : market.getShares()) {
            html.append("      <tr><td>")
                    .append(escape(row.getVendor()))
                    .append("</td><td>")
                    .append(formatUnits(row.getUnits()))
                    .append("</td><td>")
                    .append(String.format(Locale.US, "%.1f%%", row.getPercentage()))
                    .append("</td></tr>\n");
        }

        html.append("      <tr><td><strong>Total</strong></td><td><strong>")
                .append(formatUnits(market.getTotal()))
                .append("</strong></td><td><strong>100%</strong></td></tr>\n")
                .append("    </tbody>\n")
                .append("  </market>\n")
                .append("</body>\n")
                .append("</html>\n");

        return html.toString();
    }

    private String formatUnits(double units) {
        return UNIT_FORMAT.format(units);
    }

    private String escape(String raw) {
        return raw
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
