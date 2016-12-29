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

function getFreq($mysqli, $uid) {
  $sql = "SELECT what FROM orders WHERE who = '$uid' GROUP BY what ORDER BY COUNT(*) DESC";
  $results = $mysqli->query($sql);
  $row = $results->fetch_assoc();
  return $row['what'];
}

if(isset($_POST['action']) && $_POST['action'] == "fetch") {
  if(!isset($_POST['uuid'])) {
    die("No UUID");
  }
  $uuid = ($_POST['uuid']);

  $sql = "SELECT * FROM users WHERE uuid = '$uuid'";
  $results = $mysqli->query($sql);
  $row = $results->fetch_assoc();
  $row['freq'] = getFreq($mysqli, $row['uid']);
  $x = [];
  $x['json'] = $row;
  die(json_encode($x));
}

if(isset($_POST['action']) && $_POST['action'] == "order") {
  if(!isset($_POST['who']) || !isset($_POST['what']) || !isset($_POST['price']) || !isset($_POST['qty'])) {
    die("Order Details missing");
  }

  $who = $_POST['who'];
  $what = $_POST['what'];
  $price = $_POST['price'];
  $qty = $_POST['qty'];
  $when = time();

  $sql = "INSERT INTO orders (`who`,`what`,`price`,`qty`,`when`,`paid`) VALUES ('$who', '$what', $price, $qty, '$when', 1)";
  $results = $mysqli->query($sql);
  $row = $mysqli->affected_rows();
  $x = [];
  $x['json'] = $row;
  die(json_encode($x));
}
