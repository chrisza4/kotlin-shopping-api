package com.example.shopping_api.repositories

import com.example.shopping_api.domain.PurchaseRequest
import org.springframework.data.repository.CrudRepository


interface PurchaseRequestRepository: CrudRepository<PurchaseRequest, Long?> {

}