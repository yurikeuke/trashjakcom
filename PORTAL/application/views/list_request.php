<div class="content-wrapper">
        <div class="page-title">
          <div>
            <h1><i class="fa fa-th-list"></i> Daftar Request Masyarakat</h1>
            <p>Lorem ipsum</p>
          </div>
          <div>
            <ul class="breadcrumb side">
              <li><i class="fa fa-home fa-lg"></i></li>
              <li>Tables</li>
              <li class="active"><a href="#">Data Table</a></li>
            </ul>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12">
            <div class="card">
              <div class="card-body">
                <table class="table table-hover table-bordered" id="sampleTable">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Nama Pelapor</th>
                      <th>Tanggal</th>
                      <th>Lokasi Sampah</th>
                      <th>Uraian</th>
                      <th>Attachment</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
          <?php
          if(count($list)>0){
            $i=0;
            foreach ($list as $k) {
              $base = base_url();
              echo '
                <tr>
                  <td>'.$k->id_laporan.'</td>
                  <td>'.$k->name.'</td>
                  <td>'.$k->date.'</td>
                  <td>'.$k->locaction.', RT '.$k->rt.', RW '.$k->rw.' '.$k->cities.', '.$k->district.', '.$k->villages.', '.$k->provincies.'</td>
                  <td>'.$k->description.'</td>
                  <td>Download</td>
                  <td><a href="'.$base.'admin/detail_request?id='.$k->id_laporan.'">'.$k->status.'</a></td>
                </tr>
              ';
                $i++;
              }
          }else{
           echo'
              <tr colspan="6">
                <td>Tidak ada data</td>
              </tr>
           ';
          }
          ?>
                   
                   
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <script type="text/javascript">$('#sampleTable').DataTable();</script>