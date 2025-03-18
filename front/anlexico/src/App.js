import logo from './logo.svg';
import './App.css';
import SeleccionarArchivo from './components/SeleccionarArchivo';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Analizador Léxico</h1> {/* Título agregado */}
        <button
          onClick={() => alert("Analizando...")}
          style={{ padding: '10px 20px', fontSize: '16px', cursor: 'pointer' }}
        >
          Analizar
        </button>
        <img src={logo} className="App-logo" alt="logo" />
          <SeleccionarArchivo/>
  
      </header>
    </div>
  );
}

export default App;
