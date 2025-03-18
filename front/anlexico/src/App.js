import './App.css';
import SeleccionarArchivo from './components/SeleccionarArchivo';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Analizador Léxico</h1>
          <SeleccionarArchivo/>
      </header>
    </div>
  );
}

export default App;
