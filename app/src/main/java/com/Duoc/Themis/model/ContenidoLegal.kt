package com.Duoc.Themis.model

data class DocumentoLegal(
    val titulo: String,
    val referencia: String,
    val resumen: String
)

val LEYES_CHILE = listOf(
    DocumentoLegal(
        titulo = "Ley N° 19.496 - Protección de los Derechos de los Consumidores",
        referencia = "Ley N° 19.496 (publicada el 7 de marzo de 1997)",
        resumen = "Establece las normas que protegen a las personas cuando compran bienes o contratan " +
                "servicios. Reconoce derechos básicos del consumidor, como recibir información veraz y " +
                "oportuna, elegir libremente, no ser discriminado arbitrariamente, tener seguridad en el " +
                "consumo y ser reparado o indemnizado por los daños sufridos. También fija obligaciones " +
                "para los proveedores, regula los contratos de adhesión y la garantía legal ante productos " +
                "defectuosos (reparación, cambio o devolución del dinero). Los reclamos se tramitan ante " +
                "los Juzgados de Policía Local, y el SERNAC vela por su cumplimiento."
    ),
    DocumentoLegal(
        titulo = "Ley N° 20.609 - Medidas contra la Discriminación (Ley Zamudio)",
        referencia = "Ley N° 20.609 (publicada el 24 de julio de 2012)",
        resumen = "Crea un mecanismo judicial para restablecer los derechos de quien sufre " +
                "discriminación arbitraria. Define como tal toda distinción, exclusión o restricción sin " +
                "justificación razonable que cause privación, perturbación o amenaza en el ejercicio de " +
                "derechos fundamentales, por motivos como raza, religión, sexo, orientación sexual, " +
                "discapacidad o situación socioeconómica. Establece la acción de no discriminación ante " +
                "el juez civil y además incorpora en el Código Penal una agravante para delitos " +
                "motivados por discriminación."
    )
)

val ARTICULOS_CHILE = listOf(
    DocumentoLegal(
        titulo = "Código Penal - Artículo 1°",
        referencia = "Código Penal de Chile, Art. 1° (vigente desde 1875)",
        resumen = "Define qué es un delito: una acción u omisión voluntaria que la ley castiga con " +
                "una pena. De esta definición se desprende que no hay delito sin una conducta voluntaria " +
                "y sin una ley previa que la sancione, lo que es la base del principio de legalidad en el " +
                "derecho penal chileno."
    ),
    DocumentoLegal(
        titulo = "Código Penal - Artículo 432",
        referencia = "Código Penal de Chile, Art. 432 (Robo y hurto)",
        resumen = "Define el robo y el hurto. Comete robo quien, sin la voluntad del dueño y con ánimo " +
                "de lucro, se apropia de una cosa mueble ajena usando violencia o intimidación en las " +
                "personas, o fuerza en las cosas. Si faltan esa violencia, intimidación o fuerza, la " +
                "conducta constituye hurto."
    )
)