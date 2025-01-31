<?php
function determinarGanador($jugador1, $jugador2) {
    $opciones = ["Piedra", "Papel", "Tijera", "Lagarto", "Spock"];
    
    // Matriz de resultados según las reglas del juego
    $reglas = [
        [0, -1, 1, 1, -1], // Piedra
        [1, 0, -1, -1, 1], // Papel
        [-1, 1, 0, 1, -1], // Tijera
        [-1, 1, -1, 0, 1], // Lagarto
        [1, -1, 1, -1, 0]  // Spock
    ];

    echo "Jugador 1 eligió: {$opciones[$jugador1]}\n";
    echo "Jugador 2 eligió: {$opciones[$jugador2]}\n";

    $resultado = $reglas[$jugador1][$jugador2];

    if ($resultado === 1) {
        echo "¡Jugador 1 gana!\n";
    } elseif ($resultado === -1) {
        echo "¡Jugador 2 gana!\n";
    } else {
        echo "¡Es un empate!\n";
    }
}

// Leer argumentos de línea de comandos
if ($argc !== 3) {
    echo "Uso: php piedra_papel_tijera_lagarto_spock.php <jugador1> <jugador2>\n";
    exit(1);
}

$jugador1 = (int) $argv[1];
$jugador2 = (int) $argv[2];

if ($jugador1 < 0 || $jugador1 > 4 || $jugador2 < 0 || $jugador2 > 4) {
    echo "Los valores de entrada deben estar entre 0 y 4.\n";
    exit(1);
}

determinarGanador($jugador1, $jugador2);
?>
