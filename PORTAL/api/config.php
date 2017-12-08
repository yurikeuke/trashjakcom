<?php
date_default_timezone_set('Asia/Jakarta');
//koneksi ke database

$servername = "localhost";
$username   = "yramlcom_trjak";
$password   = "CGHme-^Br7[u";
$database   = "yramlcom_tra5hjak";

// Create connection
$conn = new mysqli($servername, $username, $password, $database);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

// ------------------------------------------------------------------------

?>