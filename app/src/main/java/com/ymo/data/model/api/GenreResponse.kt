package com.ymo.data.model.api

import com.google.gson.annotations.SerializedName

data class GenreResponse(
	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null
)

