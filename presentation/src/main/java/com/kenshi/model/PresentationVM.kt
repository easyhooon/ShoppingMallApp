package com.kenshi.model

import com.kenshi.domain.model.BaseModel

abstract class PresentationVM<T : BaseModel>(val model: T) {
    // some func..
}