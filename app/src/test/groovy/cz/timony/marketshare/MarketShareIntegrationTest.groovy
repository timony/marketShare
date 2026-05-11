package cz.timony.marketshare

import cz.timony.marketshare.domain.Market
import cz.timony.marketshare.input.InputRecordReaderProvider
import spock.lang.Specification

class MarketShareIntegrationTest extends Specification {

    def 'should load market share data from CSV and calculate percentages correctly'() {
        given:
        def csvFile = 'src/test/resources/market_share_data.csv'
        def inputRecords = InputRecordReaderProvider.provide(new File(csvFile)).read()

        Market market = new MarketBuilder().build(inputRecords)

        expect:
        with(market.findShare('Acer', '2010 Q3').get()) {
            it.units == 11376923483
            it.percentage == new BigDecimal('13.1')
        }
        market.getRowNumberOfVendor('Acer').getAsInt() == 1
        with(market.findShare('Acer', '2010 Q4').get()) {
            it.units == 6172256622
            it.percentage == new BigDecimal('7.1')
        }

        market.findShare('Karel', '2010 Q3').isEmpty()
    }
}
