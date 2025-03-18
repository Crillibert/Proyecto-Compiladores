import React, { useState } from "react";

const SeleccionarArchivo = () => {
  const [direccion, setDireccion] = useState(""); 
  const [direccionFinal, setDireccionFinal] = useState("");

  const manejarSubmit = (e) => {
    e.preventDefault();
    setDireccionFinal(direccion);
  };

  return (
    <form onSubmit={manejarSubmit}>
      <div>
        <p>El archivo está en: {direccionFinal}</p>
        <input
          name="direccion"
          type="text"
          placeholder="Ingresa tu código aquí..."
          style={{ width: '50%', height: '150px', margin: '20px', padding: '10px', fontSize: '16px' }}
          value={direccion}
          onChange={(e) => setDireccion(e.target.value)}
        />
        <button type="submit"
          onClick={() => alert("Analizando...")}
          style={{ padding: '10px 20px', fontSize: '16px', cursor: 'pointer' }}
        >Analizar</button>
      </div>
    </form>
  );
};

export default SeleccionarArchivo;
