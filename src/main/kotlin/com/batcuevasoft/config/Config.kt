package com.batcuevasoft.config

class Config(
    val host: String,
    val port: Int,
    val databaseHost: String,
    val databasePort: String
) {

    companion object {
        const val DATABASENAME: String = "washington"
        const val DATABASEUSER: String = "washington"
        const val DATABASEPASSWORD: String = "washington"
    }
}
