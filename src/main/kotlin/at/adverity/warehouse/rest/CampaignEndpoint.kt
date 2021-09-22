package at.adverity.warehouse.rest

import at.adverity.warehouse.Constants.datePattern
import at.adverity.warehouse.entity.CampaignData
import at.adverity.warehouse.entity.QCampaignData
import at.adverity.warehouse.repository.CampaignDataRepository
import at.adverity.warehouse.rest.model.*
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import javax.persistence.EntityManager

@RestController
@RequestMapping("campaigns")
class CampaignEndpoint(
    private val repository: CampaignDataRepository,
    private val em: EntityManager
) {

    @GetMapping("")
    fun query(
        @RequestParam(defaultValue = "")
        filterByFields: List<FilterByParam>,
        @RequestParam(defaultValue = "")
        filterByValues: List<String>,
    ): MutableIterable<CampaignData> {
        assert(filterByFields.size == filterByValues.size)
        val campaignData = QCampaignData.campaignData

        val predicate = createFilterPredicate(
            filterByFields,
            filterByValues,
            campaignData
        )

        return repository.findAll(predicate)
    }

    @GetMapping("aggregate")
    fun query(
        @RequestParam
        aggregateBy: AggregateByParam,
        @RequestParam
        aggregateField: AggregateFieldParam,
        @RequestParam
        groupByFields: List<GroupByFieldParam>,
        @RequestParam(defaultValue = "")
        filterByFields: List<FilterByParam>,
        @RequestParam(defaultValue = "")
        filterByValues: List<String>,
    ): List<AggregateResult>? {
        assert(filterByFields.size == filterByValues.size)

        val campaignData = QCampaignData.campaignData
        val predicate = createFilterPredicate(
            filterByFields,
            filterByValues,
            campaignData
        )
        val aggregateFieldPath = when (aggregateField) {
            AggregateFieldParam.Datasource -> campaignData.datasource
            AggregateFieldParam.Campaign -> campaignData.campaign
            AggregateFieldParam.Date -> campaignData.date
            AggregateFieldParam.Clicks -> campaignData.clicks
            AggregateFieldParam.Impressions -> campaignData.impressions
        }
        val groupByFieldPath = groupByFields.map {
            mapGroupByToPath(
                it,
                campaignData
            )
        }
            .toTypedArray()
        val aggregation = when {
            aggregateBy == AggregateByParam.Min -> aggregateFieldPath.min()
            aggregateBy == AggregateByParam.Max -> aggregateFieldPath.max()
            aggregateBy == AggregateByParam.Avg && aggregateFieldPath is NumberPath<*> -> aggregateFieldPath.avg()
            aggregateBy == AggregateByParam.Sum && aggregateFieldPath is NumberPath<*> -> aggregateFieldPath.sum()
            else -> throw IllegalArgumentException("Invalid aggregateBy[{$aggregateBy}] and aggregateField[${aggregateFieldPath}] combination!")
        }

        val result = JPAQuery<Void>(em)
            .select(
                *groupByFieldPath,
                aggregation
            )
            .from(campaignData)
            .where(predicate)
            .groupBy(*groupByFieldPath)
            .fetch()
        return result.map { tuple ->
            val toArray = tuple.toArray()
                .map { it.toString() }
            AggregateResult(
                *toArray.dropLast(1)
                    .toTypedArray(),
                toArray.last()
            )
        }
    }

    private fun mapGroupByToPath(
        groupByFieldParam: GroupByFieldParam,
        campaignData: QCampaignData,
    ): Expression<*> {
        return when (groupByFieldParam) {
            GroupByFieldParam.Datasource -> campaignData.datasource
            GroupByFieldParam.Campaign -> campaignData.campaign
            GroupByFieldParam.Date -> campaignData.date
            GroupByFieldParam.Clicks -> campaignData.clicks
            GroupByFieldParam.Impressions -> campaignData.impressions
        }
    }

    private fun createFilterPredicate(filterByFields: List<FilterByParam>,
                                      filterByValues: List<String>,
                                      campaignData: QCampaignData
    ): BooleanBuilder {
        val predicate = filterByFields.zip(filterByValues)
            .fold(BooleanBuilder()) { query, filter ->
                val (filterField, filterValue) = filter
                when (filterField) {
                    FilterByParam.Datasource -> query.and(campaignData.datasource.equalsIgnoreCase(filterValue))
                    FilterByParam.Campaign -> query.and(campaignData.campaign.equalsIgnoreCase(filterValue))
                    FilterByParam.StartDate -> query.and(
                        campaignData.date.goe(
                            LocalDate.parse(
                                filterValue,
                                datePattern
                            )
                        )
                    )
                    FilterByParam.EndDate -> query.and(
                        campaignData.date.before(
                            LocalDate.parse(
                                filterValue,
                                datePattern
                            )
                        )
                    )
                    FilterByParam.Clicks -> query.and(campaignData.clicks.eq(filterValue.toLong()))
                    FilterByParam.Impressions -> query.and(campaignData.impressions.eq(filterValue.toLong()))
                }
            }
        return predicate
    }

}