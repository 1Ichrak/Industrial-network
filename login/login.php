<?php
session_start();

header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "securolab_db";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
  echo json_encode(['success' => false, 'message' => "DB Connection failed"]);
  exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  $scientist_id = $_POST['identifier'];
  $plain_password = $_POST['password'];

  $stmt = $conn->prepare("SELECT password FROM scientists WHERE scientist_id = ?");
  $stmt->bind_param("s", $scientist_id);
  $stmt->execute();
  $stmt->store_result();

  if ($stmt->num_rows === 1) {
    $stmt->bind_result($hashed_password);
    $stmt->fetch();

    if (password_verify($plain_password, $hashed_password)) {
      $_SESSION['scientist_id'] = $scientist_id;
      echo json_encode(['success' => true]);
    } else {
      echo json_encode(['success' => false, 'message' => 'Incorrect password']);
    }
  } else {
    echo json_encode(['success' => false, 'message' => 'Scientist ID not found']);
  }

  $stmt->close();
  $conn->close();
  exit();
}
?>
