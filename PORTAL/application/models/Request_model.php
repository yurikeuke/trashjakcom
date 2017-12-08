<?php
  class Request_model extends CI_Model {
 
   public function list_request() {
      	$this->db->select('
      		id_laporan, 
      		name, 
      		email, 
      		date, 
      		locaction, 
      		rt, 
      		rw, 
      		cities, 
      		district, 
      		villages, 
      		provincies, 
      		description, 
      		attachment, 
      		status');
        $this->db->where("is_removed",'0');
        $this->db->from('tj_requests');
		$query = $this->db->get();
        return $query;
      }


   public function detail($id) {
      	$this->db->where("id_laporan", $id);
        $this->db->where("is_removed",'0');
        $this->db->from('tj_requests');
		$query = $this->db->get();
        return $query;
  }
  public function assignee() {
        $this->db->where("role", 'petugas');
        $this->db->where("is_removed",'0');
        $this->db->from('tj_user');
    $query = $this->db->get();
        return $query;
   }

//endoffile
  }
?>