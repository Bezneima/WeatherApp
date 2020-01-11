import com.google.gson.annotations.SerializedName

data class Day (

	@SerializedName("_source") val _source : String,
	@SerializedName("temp_min") val temp_min : Double,
	@SerializedName("temp_max") val temp_max : Double,
	@SerializedName("temp_avg") val temp_avg : Double,
	@SerializedName("feels_like") val feels_like : Double,
	@SerializedName("icon") val icon : String,
	@SerializedName("condition") val condition : String,
	@SerializedName("daytime") val daytime : String,
	@SerializedName("polar") val polar : Boolean,
	@SerializedName("wind_speed") val wind_speed : Double,
	@SerializedName("wind_gust") val wind_gust : Double,
	@SerializedName("wind_dir") val wind_dir : String,
	@SerializedName("pressure_mm") val pressure_mm : Double,
	@SerializedName("pressure_pa") val pressure_pa : Double,
	@SerializedName("humidity") val humidity : Double,
	@SerializedName("uv_index") val uv_index : Double,
	@SerializedName("soil_temp") val soil_temp : Double,
	@SerializedName("soil_moisture") val soil_moisture : Double,
	@SerializedName("prec_mm") val prec_mm : Double,
	@SerializedName("prec_period") val prec_period : Double,
	@SerializedName("prec_prob") val prec_prob : Double
)