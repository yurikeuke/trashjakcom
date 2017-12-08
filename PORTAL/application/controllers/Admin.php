<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Admin extends CI_Controller {

	/**
	 * Index Page for this controller.
	 *
	 * Maps to the following URL
	 * 		http://example.com/index.php/welcome
	 *	- or -
	 * 		http://example.com/index.php/welcome/index
	 *	- or -
	 * Since this controller is set as the default controller in
	 * config/routes.php, it's displayed at http://example.com/
	 *
	 * So any other public methods not prefixed with an underscore will
	 * map to /index.php/welcome/<method_name>
	 * @see https://codeigniter.com/user_guide/general/urls.html
	 */
	function __construct() {
    parent::__construct();
	    if ($this->session->userdata('username')) {
	      redirect(base_url('admin/dashboard'));
	    }
	    $this->load->model(array('Login_model'));
  	}

	public function index()
	{
		$this->load->view('header');
		$this->load->view('sign-in');
	}

	public function login() {
	    $this->form_validation->set_rules('username', 'username', 'required|trim|xss_clean');
	    $this->form_validation->set_rules('password', 'password', 'required|trim|xss_clean');
	    if ($this->form_validation->run() == FALSE) {
	      $this->load->view('sign-in');
	     } else {
	      $usr = $this->input->post('username');
	      $psw = $this->input->post('password');
	      $passmd = md5($psw);
	      $cek = $this->Login_model->cek($usr, $passmd);
	      if ($cek->num_rows() > 0) {
	        //login berhasil, buat session
	        foreach ($cek->result() as $qad) {
	          $sess_data['id'] = $qad->id;
	          $sess_data['name'] = $qad->name;
	          $sess_data['role'] = $qad->role;
	          $this->session->set_userdata($sess_data);
	        }
	        $this->session->set_flashdata('success', 'Login Berhasil !');
	        redirect(base_url('admin/dashboard'));
	      } else {
		    $this->session->set_flashdata('result_login', 'Username atau Password yang anda masukkan salah.');
		    redirect(base_url(''));
		      }
	    }
	  }

	public function dashboard(){
		$this->load->view('header_ad');
		$this->load->view('dashboard');
	}

	public function logout(){
	  $this->session->sess_destroy();
	  redirect(base_url(''));
	 }

	public function list_request(){
		$this->load->model('Request_model');
		$list = $this->Request_model->list_request();
		//var_dump($list->result());
		$data['list'] 	= $list->result();
		$this->load->view('header_ad',$data);
		$this->load->view('list_request',$data);
	}

	public function detail_request(){
		$id = $_GET['id'];
		$this->load->model('Request_model');
		$req = $this->Request_model->detail($id);
		$assg = $this->Request_model->assignee();
		//var_dump($list->result());
		$data['detail'] 	= $req->result();
		$data['petugas'] 	= $assg->result();
		$this->load->view('header_ad',$data);
		$this->load->view('detail_request',$data);
	}
// end of file
}
