package at.adverity.warehouse

import at.adverity.warehouse.service.CsvParserService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WarehouseApplication

fun main(args: Array<String>) {
    runApplication<WarehouseApplication>(*args)
        .getBean(CsvParserService::class.java)
        .importData()
}
