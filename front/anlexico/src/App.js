import React, { useState } from 'react';
import './App.css';
import SeleccionarArchivo from './components/SeleccionarArchivo';

function App() {
  const [codigo, setCodigo] = useState('');
  const [resultado, setResultado] = useState([]);
  const [archivo, setArchivo] = useState(null);
  const [esArchivo, setEsArchivo] = useState(false);

  const analizarCodigo = () => {
    if (esArchivo && archivo) {
      const reader = new FileReader();
      reader.onload = (e) => {
        const contenido = e.target.result;
        enviarCodigo(contenido);
      };
      reader.readAsText(archivo);
    } else {
      enviarCodigo(codigo);
    }
  };

  const enviarCodigo = (codigo) => {
    fetch('http://localhost:8080/analizador/analizar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ codigo }),
    })
      .then((res) => res.json())
      .then((data) => {
        setResultado(data.tokens);  // Guardamos los tokens como array
      })
      .catch((err) => console.error('Error:', err));
  };

  const handleCodigoChange = (e) => {
    setCodigo(e.target.value);
    setEsArchivo(false);
  };

  const handleArchivoChange = (e) => {
    setArchivo(e.target.files[0]);
    setEsArchivo(true);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Analizador Léxico</h1>

        <div>
          <textarea
            value={codigo}
            onChange={handleCodigoChange}
            placeholder="Ingresa tu código aquí"
            rows="10"
            cols="50"
          />
        </div>

        <div>
          <input
            type="file"
            onChange={handleArchivoChange}
            accept=".txt"
          />
        </div>

        <button onClick={analizarCodigo}>Analizar</button>
        
        {resultado.length > 0 && ( //Lógica para tabla de resultados
          <div>
            <h3>Resultado:</h3>
            <table className="resultado-tabla">
              <thead>
                <tr>
                  <th>Token</th>
                  <th>Tipo</th>
                </tr>
              </thead>
              <tbody>
                {resultado.map((token, index) => {
                  // Cada token viene como "valor -> Tipo", separamos
                  const [valor, tipo] = token.split(' -> ');
                  return (
                    <tr key={index}>
                      <td>{valor}</td>
                      <td>{tipo}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </header>
    </div>
  );
}

export default App;
