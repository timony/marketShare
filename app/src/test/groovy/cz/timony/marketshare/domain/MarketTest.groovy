package cz.timony.marketshare.domain

import spock.lang.Specification

class MarketTest extends Specification {

    def 'should the calculated percentages sum to 100%'() {
        given:
        def random = new Random()
        def shares = (1..size).collect {
            new Share(
                    "Vendor $it",
                    '2010 Q3',
                    random.nextLong(100000 - 10000) + 10000
            )
        }

        when:
        def market = new Market(shares)

        then:
        market.shares.size() == size
        market.shares.sum { it.percentage } == 100.0

        where:
        size << [1, 10, 50, 100, 200]
    }

    def 'should calculate total units correctly'() {
        given:
        def shares = [
                new Share('Vendor A', '2010 Q3', 10000),
                new Share('Vendor B', '2010 Q3', 20000),
                new Share('Vendor C', '2010 Q3', 5000)
        ]

        when:
        def market = new Market(shares)

        then:
        market.total == 35000
    }

    def 'should group and merge multiple records of the same vendor and quoter'() {
        given:
        def shares = [
                new Share('Vendor A', '2010 Q3', 10000),
                new Share('Vendor B', '2010 Q3', 20000),
                new Share('Vendor A', '2010 Q3', 15000),
                new Share('Vendor C', '2010 Q3', 5000)
        ]

        when:
        def market = new Market(shares)

        then:
        market.shares.size() == 3
        market.shares.find { it.vendor == 'Vendor A' }.units == 25000
        market.shares.find { it.vendor == 'Vendor B' }.units == 20000
        market.shares.find { it.vendor == 'Vendor C' }.units == 5000
    }

    def 'should handle empty shares list'() {
        given:
        def shares = []

        when:
        def market = new Market(shares)

        then:
        market.shares.isEmpty()
    }

    def 'should handle zero total units'() {
        given:
        def shares = [
                new Share('Vendor A', '2010 Q3', 0),
                new Share('Vendor B', '2010 Q3', 0),
                new Share('Vendor C', '2010 Q3', 0)
        ]

        when:
        def market = new Market(shares)

        then:
        market.shares.size() == 3
        market.total == 0
    }
}