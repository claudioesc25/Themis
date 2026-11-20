package com.Duoc.Themis.model

enum class TipoLugar(val etiqueta: String) {
    BIBLIOTECA("Biblioteca"),
    FISCALIA("Fiscalía")
}

data class Lugar(
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val tipo: TipoLugar
)

// IMPORTANTE: las coordenadas de abajo son APROXIMADAS (de ejemplo).
// Verifícalas y agrega los lugares que necesites:
//   Google Maps -> mantén presionado el punto exacto -> arriba aparecen
//   las coordenadas (ej: -33.4424, -70.6437) -> tócalas para copiarlas.
val LUGARES = listOf(
    // ---- Bibliotecas ----
    Lugar(
        "Biblioteca Nacional de Chile",
        "Av. Libertador Bernardo O'Higgins 651, Santiago",
        -33.4424, -70.6437,
        TipoLugar.BIBLIOTECA
    ),
    Lugar(
        "Biblioteca de Santiago",
        "Matucana 151, Santiago",
        -33.4411, -70.6807,
        TipoLugar.BIBLIOTECA
    ),

    // Dirección encontrada en documentos oficiales; coordenadas APROXIMADAS (verificar)
    Lugar(
        "Centro Bibliotecario de Puente Alto",
        "Concha y Toro 507, Puente Alto",
        -33.6105, -70.5760,
        TipoLugar.BIBLIOTECA
    ),
    Lugar(
        "Biblioteca del Congreso Nacional",
        "Compañía de Jesús 1175, Santiago",
        -33.4393, -70.6567,
        TipoLugar.BIBLIOTECA
    ),

    // ---- Fiscalías ----
    // Direcciones tomadas de documentos del Ministerio Público / Poder Judicial.
    // Las COORDENADAS son aproximadas: VERIFÍCALAS en Google Maps (mantén
    // presionado el punto exacto y copia los números).
    Lugar(
        "Fiscalía Nacional (Ministerio Público)",
        "Catedral 1437, Santiago",
        -33.4375, -70.6602,
        TipoLugar.FISCALIA
    ),
    Lugar(
        "Fiscalía Regional Metropolitana Sur",
        "Gran Avenida 5234, San Miguel",
        -33.5070, -70.6528,
        TipoLugar.FISCALIA
    ),
    Lugar(
        "Fiscalía Local (atendía Puente Alto) - San Miguel",
        "Gran Avenida José Miguel Carrera 3814, San Miguel (dirección de un documento de 2021)",
        -33.4950, -70.6516,
        TipoLugar.FISCALIA
    ),
    Lugar(
        "Fiscalía Local La Florida",
        "Av. Américo Vespucio 6800, La Florida",
        -33.5230, -70.5960,
        TipoLugar.FISCALIA
    ),
    Lugar(
        "Fiscalía Local San Bernardo",
        "San José 840, San Bernardo",
        -33.5920, -70.7000,
        TipoLugar.FISCALIA
    ),
    Lugar(
        "Fiscalía Regional Metropolitana Centro Norte",
        "Av. Pedro Montt 1606 (Centro de Justicia), Santiago",
        -33.4510, -70.6690,
        TipoLugar.FISCALIA
    )

    // Confirma en fiscaliadechile.cl cuál es hoy la fiscalía que atiende Puente Alto.

    // Agrega aquí las bibliotecas y fiscalías de tu zona (Puente Alto, etc.):
    // Lugar("Nombre", "Dirección", latitud, longitud, TipoLugar.BIBLIOTECA),
)