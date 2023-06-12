package com.kenshi.model

import com.kenshi.delegate.ProductDelegate
import com.kenshi.domain.model.Product

class ProductVM(model: Product, productDelegate: ProductDelegate) : PresentationVM<Product>(model), ProductDelegate by productDelegate