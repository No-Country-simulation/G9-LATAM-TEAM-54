async function analizar(){

    const boton = document.querySelector("button[onclick='analizar()']");
    const errorBox = document.getElementById("error-box");
    if(errorBox) errorBox.remove();

    const consumo = Number(document.getElementById("consumo").value);

    const equipos = Number(document.getElementById("equipos").value);

    const tipo = document.getElementById("tipo").value;

    const horas = Number(document.getElementById("horas").value);

    const pico = document.getElementById("pico").value === "true";


    const body = {

        consumo_kwh: consumo,

        uso_horario_pico: pico,

        cantidad_equipos: equipos,

        tipo_inmueble: tipo,

        horas_alto_consumo: horas

    };

    const textoOriginal = boton.innerHTML;
    boton.disabled = true;
    boton.innerHTML = "Analizando...";

    try{

        /*
        Cuando el backend esté listo:

        const response = await fetch(
            "http://localhost:8080/analisis-energetico",
            {
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                },
                body:JSON.stringify(body)
            });

        const data = await response.json();
        */


        //Datos simulados

        const data = {

            categoria:"Moderado",

            probabilidad:0.86,

            costoEstimadoMensual:315,

            recomendaciones:[

                "Reducir consumo durante horario pico",

                "Revisar equipos antiguos",

                "Distribuir actividades de mayor consumo"

            ]

        };


        document.getElementById("categoria").innerText =
        data.categoria;

        document.getElementById("probabilidad").innerText =
        (data.probabilidad*100).toFixed(1)+"%";

        document.getElementById("costo").innerText =
        data.costoEstimadoMensual.toFixed(2);


        const lista =
        document.getElementById("recomendaciones");

        lista.innerHTML="";

        data.recomendaciones.forEach(r=>{

            lista.innerHTML+=`<li>${r}</li>`;

        });

    }

    catch(error){

        mostrarError("Error al conectar con la API. Intenta de nuevo en unos segundos.");

        console.error(error);

    }

    finally{
        boton.disabled = false;
        boton.innerHTML = textoOriginal;
    }

}

function mostrarError(mensaje){
    const resultado = document.getElementById("resultado");
    const box = document.createElement("div");
    box.id = "error-box";
    box.className = "error-box";
    box.textContent = mensaje;
    resultado.prepend(box);
}