<?php
session_start();

class Buscaminas {
    private $filas;
    private $columnas;
    private $minas;

    public function __construct($nivel) {
        switch ($nivel) {
            case 'facil':
                $this->filas = 8;
                $this->columnas = 8;
                $this->minas = 10;
                break;
            case 'medio':
                $this->filas = 16;
                $this->columnas = 16;
                $this->minas = 40;
                break;
            case 'dificil':
                $this->filas = 16;
                $this->columnas = 30;
                $this->minas = 99;
                break;
            default:
                $this->filas = 8;
                $this->columnas = 8;
                $this->minas = 10;
                break;
        }
    }

    public function generarTablero() {
        $tablero = array_fill(0, $this->filas, array_fill(0, $this->columnas, 0));

        for ($i = 0; $i < $this->minas; $i++) {
            do {
                $fila = rand(0, $this->filas - 1);
                $columna = rand(0, $this->columnas - 1);
            } while ($tablero[$fila][$columna] == -1);

            $tablero[$fila][$columna] = -1;

            for ($j = $fila - 1; $j <= $fila + 1; $j++) {
                for ($k = $columna - 1; $k <= $columna + 1; $k++) {
                    if ($j >= 0 && $j < $this->filas && $k >= 0 && $k < $this->columnas && $tablero[$j][$k] != -1) {
                        $tablero[$j][$k]++;
                    }
                }
            }
        }

        return $tablero;
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents('php://input'), true);
    $nivel = $data['nivel'];

    $buscaminas = new Buscaminas($nivel);
    $tablero = $buscaminas->generarTablero();

    $_SESSION['tablero'] = $tablero;

    echo json_encode($tablero);
}
?>