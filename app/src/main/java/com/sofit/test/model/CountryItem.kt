package com.sofit.test.model

data class CountryItem(
    val alpha2Code: String?=null,
    val alpha3Code: String?=null,
    val altSpellings: List<String>?=null,
    val area: Double?=null,
    val borders: List<String>?=null,
    val callingCodes: List<String>?=null,
    val capital: String?=null,
    val cioc: String?=null,
    val currencies: List<Currency>?=null,
    val demonym: String?=null,
    val flag: String?=null,
    val flags: Flags?=null,
    val gini: Double?=null,
    val independent: Boolean?=null,
    val languages: List<Language>?=null,
    val latlng: List<Double>?=null,
    val name: String?=null,
    val nativeName: String?=null,
    val numericCode: String?=null,
    val population: Int?=0,
    val region: String?=null,
    val regionalBlocs: List<RegionalBloc>?=null,
    val subregion: String?=null,
    val timezones: List<String>?=null,
    val topLevelDomain: List<String>?=null,
    val translations: Translations?=null
)