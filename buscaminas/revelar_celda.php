<?php
session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents('php://input'), true);

    $fila = $data['fila'];
    $columna = $data['columna'];

    $tablero = $_SESSION['tablero'];
    echo json_encode([
        'valor' => $tablero[$fila][$columna],
    ]);
}
?>
