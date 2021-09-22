package at.adverity.warehouse.repository

import at.adverity.warehouse.entity.CampaignData
import at.adverity.warehouse.entity.QCampaignData
import com.querydsl.core.types.dsl.StringPath
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.data.querydsl.binding.SingleValueBinding
import java.util.*


interface CampaignDataRepository : JpaRepository<CampaignData, UUID>,
    QuerydslPredicateExecutor<CampaignData>,
    QuerydslBinderCustomizer<QCampaignData> {
    @JvmDefault
    override fun customize(bindings: QuerydslBindings,
                           root: QCampaignData
    ) {
        bindings.bind(String::class.java)
            .first(SingleValueBinding { obj: StringPath, str: String? -> obj.containsIgnoreCase(str) })
    }
}