import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Analizador Léxico</h1> {/* Título agregado */}
        <textarea
          placeholder="Ingresa tu código aquí..."
          style={{ width: '50%', height: '150px', margin: '20px', padding: '10px', fontSize: '16px' }}
        />
        <button
          onClick={() => alert("Analizando...")}
          style={{ padding: '10px 20px', fontSize: '16px', cursor: 'pointer' }}
        >
          Analizar
        </button>
      </header>
    </div>
  );
}

export default App;
