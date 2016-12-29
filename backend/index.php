<?php

$mysqli = new mysqli('localhost','root','root','cn');
if ($mysqli->connect_error) {
    die('Error : ('. $mysqli->connect_errno .')');
}

if(empty($_POST)) {
  die("Invalid");
}
if(!isset($_POST['version'])) {
  die("No version");
}

if(isset($_POST['action']) && $_POST['action'] == "fetch") {
  if(!isset($_POST['uuid'])) {
    die("No UUID");
  }
  $uuid = ($_POST['uuid']);

  $sql = "SELECT * FROM users WHERE uuid = '$uuid'";
  $results = $mysqli->query($sql);
  $row = $results->fetch_assoc();
  die(json_encode($row));
}

if(isset($_POST['action']) && $_POST['action'] == "order") {
  if(!isset($_POST['who']) || !isset($_POST['what']) || !isset($_POST['price']) || !isset($_POST['qty'])) {
    die("Order Details missing");
  }

  $who = $_POST['who'];
  $what = $_POST['what'];
  $price = $_POST['price'];
  $qty = $_POST['qty'];
  $when = date();

  $sql = "INSERT INTO orders (`who`,`what`,`price`,`qty`,`when`,`paid`) VALUES ('$who', '$what', $price, $qty, '$when', 1)";
  $results = $mysqli->query($sql);
  $row = $results->fetch_assoc();
  die(json_encode($row));
}
