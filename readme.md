# About

This project is intended as self-contained spring boot application. On startup, it will read a csv file to create an initial in memory
database.

# Endpoints

There are two endpoints: `/campaigns` and `/campaigns/aggregate`.
`/campaigns` allows to do simple queries by using the `filterByFields` and `filterByValues` request parameters. `filterByFields` indicates
the fields by which filtering should occur while `filterByValue` passes the values for filtering. Consequently, there should always be the
same amount of values in each param. Example for filtering by campaign=camp1 and clicks=10 ->
`/campaigns?filterByFields=Campaign,Clickst&filterByValues=camp1,10`
`/campaigns/aggregate` works the same way, but also supports `aggregateField` and `groupByFields` request parameters.

# FilterFields/AggregateFields

* Datasource
* Campaign
* StartDate
* EndDate
* Clicks
* Impressions

# Aggregations

* Min
* Max
* Avg
* Sum

# Groupings

* Datasource
* Campaign
* Date
* Clicks
* Impressions
