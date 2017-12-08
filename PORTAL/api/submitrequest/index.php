<?php
include_once('../config.php');

// array for final json respone
$response = array();

        $name           = $_POST['nama'];
        $email          = $_POST['email'];
        $date           = $_POST['tanggal'];
        $locaction      = $_POST['alamat'];
        $rt             = $_POST['rt'];
        $rw             = $_POST['rw'];
        $cities         = $_POST['kota'];
        $district       = $_POST['kelurahan'];
        $villages       = $_POST['kecamatan'];
        $provincies     = $_POST['provinsi'];
        $weight         = $_POST['berat'];
        $description    = $_POST['uraian'];
        $attachment     = $_POST['attachment'];
        $jenis          = $_POST['jenis'];
        $latitude       = $_POST['latitude'];
        $longitude      = $_POST['longitude'];
        
        
        $sql = "
           INSERT INTO `tj_requests`(
           `name`, 
           `email`, 
           `date`, 
           `locaction`, 
           `rt`, 
           `rw`, 
           `cities`, 
           `district`, 
           `villages`, 
           `provincies`, 
           `weight`, 
           `description`, 
           `attachment`, 
           `jenis`, 
           `latitude`, 
           `longitude`
           ) 
           VALUES (
            '".$name."',
            '".$email."',
            '".$date."',
            '".$locaction."',
            '".$rt."',
            '".$rw."',
            '".$cities."',
            '".$district."',
            '".$villages."',
            '".$provincies."',
            '".$weight."',
            '".$description."',
            '".$attachment."',
            '".$jenis."',
            '".$latitude."',
            '".$longitude."')
        ";
        //var_dump($sql);
        $result = $conn->query($sql);
   if($result) {
        $response['status_code'] = '000';
        $response['status'] = 'Sukses';
        $response['datetime'] = date('Y-m-d H:i:s');
	} else {
	    $response['status_code'] = '201';
		$response['status'] = 'Data Invalid';
		$response['datetime'] = date('Y-m-d H:i:s');
		$response['error'] = "Cannot Submit data!";

	}

 
 echo json_encode($response);

?>