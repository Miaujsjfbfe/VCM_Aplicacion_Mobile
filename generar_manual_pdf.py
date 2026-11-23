import os
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, HRFlowable
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle

def generar_pdf():
    desktop_path = os.path.expanduser("~/Desktop")
    pdf_filename = os.path.join(desktop_path, "Manual_Dev_OftApp.pdf")

    doc = SimpleDocTemplate(
        pdf_filename,
        pagesize=letter,
        rightMargin=40, leftMargin=40,
        topMargin=40, bottomMargin=40
    )

    styles = getSampleStyleSheet()

    # Custom styles
    titulo_style = ParagraphStyle(
        'TituloManual',
        parent=styles['Heading1'],
        fontSize=22,
        leading=26,
        textColor=colors.HexColor('#004D40'),
        spaceAfter=10
    )

    subtitulo_style = ParagraphStyle(
        'SubTituloManual',
        parent=styles['Normal'],
        fontSize=12,
        leading=16,
        textColor=colors.HexColor('#0F766E'),
        spaceAfter=20
    )

    h2_style = ParagraphStyle(
        'H2Manual',
        parent=styles['Heading2'],
        fontSize=15,
        leading=18,
        textColor=colors.HexColor('#0B5A4E'),
        spaceBefore=15,
        spaceAfter=8
    )

    body_style = ParagraphStyle(
        'BodyManual',
        parent=styles['Normal'],
        fontSize=10,
        leading=14,
        textColor=colors.HexColor('#2D3748'),
        spaceAfter=8
    )

    code_style = ParagraphStyle(
        'CodeManual',
        parent=styles['Normal'],
        fontSize=9,
        leading=12,
        textColor=colors.HexColor('#1E293B'),
        fontName='Courier',
        backColor=colors.HexColor('#F1F5F9'),
        borderColor=colors.HexColor('#CBD5E1'),
        borderWidth=1,
        borderPadding=6,
        spaceBefore=6,
        spaceAfter=10
    )

    story = []

    # Header
    story.append(Paragraph("Manual de Desarrollador - Microservicio Spring Boot OftApp", titulo_style))
    story.append(Paragraph("Guía técnica de arquitectura, entidades, endpoints, base de datos y documentación OpenAPI (Swagger)", subtitulo_style))
    story.append(HRFlowable(width="100%", thickness=2, color=colors.HexColor('#004D40'), spaceAfter=15))

    # 1. Introducción
    story.append(Paragraph("1. Introducción y Propósito del Backend", h2_style))
    story.append(Paragraph(
        "Este manual describe la arquitectura del microservicio backend desarrollado en <b>Spring Boot (Kotlin)</b> para el sistema <b>OftApp</b>. "
        "El microservicio centraliza la persistencia de datos clínicos, la gestión de exámenes oftalmológicos y recetas, proveyendo una API REST robusta "
        "y documentación interactiva mediante <b>Swagger / OpenAPI</b>.",
        body_style
    ))

    # 2. Estructura del Código y Ubicación
    story.append(Paragraph("2. Estructura del Código y Ubicación (Dónde encontrar la lógica)", h2_style))
    story.append(Paragraph(
        "Todo el código fuente del microservicio backend se encuentra alojado en el directorio <b>backend/oftapp-backend/</b> dentro de la raíz del proyecto. "
        "A continuación se detalla la distribución de paquetes y responsabilidades:",
        body_style
    ))

    data_files = [
        ["Componente", "Ruta del Archivo", "Responsabilidad Lógica"],
        ["Configuración Gradle", "backend/oftapp-backend/build.gradle.kts", "Dependencias de Spring Boot 3.2, Web, JPA, H2 y Springdoc."],
        ["Configuración Spring", "backend/oftapp-backend/src/main/resources/application.yml", "Puerto 8080, conexión H2 en memoria y configuración Swagger UI."],
        ["Clase Principal", "src/main/java/.../backend/OftAppBackendApplication.kt", "Punto de entrada con @SpringBootApplication y runApplication."],
        ["Entidad JPA", "src/main/java/.../backend/model/ExamenEntity.kt", "Mapeo relacional de exámenes oftalmológicos con @Entity y @Table."],
        ["Repositorio", "src/main/java/.../backend/repository/ExamenRepository.kt", "Interfaz Spring Data JPA (JpaRepository) para consultas SQL."],
        ["Controlador REST", "src/main/java/.../backend/controller/ExamenController.kt", "Endpoints HTTP (@RestController) documentados con @Operation."]
    ]

    t = Table(data_files, colWidths=[110, 220, 180])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#0F766E')),
        ('TEXTCOLOR', (0,0), (-1,0), colors.white),
        ('FONTNAME', (0,0), (-1,0), 'Helvetica-Bold'),
        ('FONTSIZE', (0,0), (-1,0), 9),
        ('BOTTOMPADDING', (0,0), (-1,0), 6),
        ('BACKGROUND', (0,1), (-1,-1), colors.HexColor('#F8FAFC')),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#CBD5E1')),
        ('FONTNAME', (0,1), (-1,-1), 'Helvetica'),
        ('FONTSIZE', (0,1), (-1,-1), 8),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
        ('TOPPADDING', (0,0), (-1,-1), 5),
    ]))
    story.append(t)
    story.append(Spacer(1, 10))

    # 3. Entidades
    story.append(Paragraph("3. Entidades y Modelo de Datos (JPA)", h2_style))
    story.append(Paragraph(
        "La entidad <code>ExamenEntity</code> representa la tabla central de exámenes en la base de datos:",
        body_style
    ))
    story.append(Paragraph(
        "@Entity<br/>@Table(name = \"examenes\")<br/>"
        "data class ExamenEntity(<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;@Id @GeneratedValue(strategy = GenerationType.IDENTITY)<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;val id: Long = 0,<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;val categoria: String,<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;val titulo: String,<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;val indicacionesMedicas: String,<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;val medico: String,<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;val fecha: String,<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;val archivoPdf: String,<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;val rutPaciente: String<br/>"
        ")",
        code_style
    ))

    # 4. Endpoints
    story.append(Paragraph("4. Endpoints REST expuestos", h2_style))
    story.append(Paragraph(
        "El controlador <code>ExamenController</code> expone la API REST bajo la ruta base <code>/api/v1/examenes</code>:",
        body_style
    ))
    story.append(Paragraph(
        "• <b>GET /api/v1/examenes</b>: Lista todos los exámenes clínicos registrados.<br/>"
        "• <b>POST /api/v1/examenes</b>: Registra y almacena un nuevo examen clínico en formato JSON.<br/>"
        "• <b>GET /api/v1/examenes/paciente/{rut}</b>: Filtra los exámenes clínicos por el RUT del paciente.",
        body_style
    ))

    # 5. Base de Datos
    story.append(Paragraph("5. Base de Datos H2 y Persistencia", h2_style))
    story.append(Paragraph(
        "El microservicio utiliza una base de datos embebida <b>H2 en memoria</b> (configurada en <code>application.yml</code>). "
        "Hibernate (JPA) genera automáticamente las tablas al iniciar la aplicación. Se puede acceder a la consola web de H2 en <code>http://localhost:8080/h2-console</code>.",
        body_style
    ))

    # 6. Swagger / OpenAPI
    story.append(Paragraph("6. Documentación Interactiva (Swagger / OpenAPI)", h2_style))
    story.append(Paragraph(
        "Gracias a la librería <b>springdoc-openapi</b>, el microservicio genera automáticamente una interfaz web interactiva con <b>Swagger UI</b>. "
        "Una vez que el servidor Spring Boot esté en ejecución, puedes abrir el navegador y entrar a:<br/>"
        "<b>http://localhost:8080/swagger-ui/index.html</b><br/>"
        "Desde allí podrás inspeccionar los contratos de la API y ejecutar peticiones directamente desde el navegador.",
        body_style
    ))

    doc.build(story)
    print(f"PDF generado exitosamente en: {pdf_filename}")

if __name__ == "__main__":
    generar_pdf()
