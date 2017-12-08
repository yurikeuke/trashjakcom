
      <div class="content-wrapper">
        <div class="page-title">
          <div>
            <h1><i class="fa fa-dashboard"></i> Dashboard</h1>
          </div>
          <div>
            <ul class="breadcrumb">
              <li><i class="fa fa-home fa-lg"></i></li>
              <li><a href="#">Dashboard</a></li>
            </ul>
          </div>
        </div>
        <div class="row">
        <!-- total -->
        <div class="col-md-3">
            <div class="widget-small primary"><i class="icon fa fa-truck fa-3x"></i>
              <div class="info">
                <h4>JAKPUS</h4>
                <p><b>5</b></p>
              </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="widget-small primary"><i class="icon fa fa-truck fa-3x"></i>
              <div class="info">
                <h4>JAKTIM</h4>
                <p><b>5</b></p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="widget-small primary"><i class="icon fa fa-truck fa-3x"></i>
              <div class="info">
                <h4>JAKBAR</h4>
                <p><b>5</b></p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="widget-small primary"><i class="icon fa fa-truck fa-3x"></i>
              <div class="info">
                <h4>JAKSEL</h4>
                <p><b>5</b></p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="widget-small primary"><i class="icon fa fa-truck fa-3x"></i>
              <div class="info">
                <h4>JAKUT</h4>
                <p><b>5</b></p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="widget-small primary"><i class="icon fa fa-truck fa-3x"></i>
              <div class="info">
                <h4>KEP.SERIBU</h4>
                <p><b>5</b></p>
              </div>
            </div>
          </div>

        <div class="col-md-12">
            <div class="card">
              <h3 class="card-title">Peta Pergerakan Trashjak</h3>
              <div class="card-body">
                <div id="map" style="height: 500px"></div>
              </div>
            </div>
          </div>
        <div class="col-md-4">
            <div class="card">
              <h3 class="card-title">Polar Chart</h3>
              <div class="embed-responsive embed-responsive-16by9">
                <canvas class="embed-responsive-item" id="polarChartDemo"></canvas>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="card">
              <h3 class="card-title">Pie Chart</h3>
              <div class="embed-responsive embed-responsive-16by9">
                <canvas class="embed-responsive-item" id="pieChartDemo"></canvas>
              </div>
            </div>
          </div>
        <div class="col-md-4">
          <div id="piechart" style="width: 280px; height:280px;"></div>
        </div>

        </div>
      </div>
    </div>
    <!-- Javascripts-->
     
    <script type="text/javascript">
      $(document).ready(function(){
        var map = $('#demo-map');
        map.vectorMap({
          map: 'world_en',
          backgroundColor: '#fff',
          color: '#333',
          hoverOpacity: 0.7,
          selectedColor: '#666666',
          enableZoom: true,
          showTooltip: true,
          scaleColors: ['#C8EEFF', '#006491'],
          values: sample_data,
          normalizeFunction: 'polynomial'
        });
      });

      var data = {
        labels: ["January", "February", "March", "April", "May"],
        datasets: [
          {
            label: "My First dataset",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: [65, 59, 80, 81, 56]
          },
          {
            label: "My Second dataset",
            fillColor: "rgba(151,187,205,0.2)",
            strokeColor: "rgba(151,187,205,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(151,187,205,1)",
            data: [28, 48, 40, 19, 86]
          }
        ]
      };
      var pdata = [
        {
          value: 300,
          color:"#F7464A",
          highlight: "#FF5A5E",
          label: "Red"
        },
        {
          value: 50,
          color: "#46BFBD",
          highlight: "#5AD3D1",
          label: "Green"
        },
        {
          value: 100,
          color: "#FDB45C",
          highlight: "#FFC870",
          label: "Yellow"
        }
      ]
      
      
      var ctxpo = $("#polarChartDemo").get(0).getContext("2d");
      var barChart = new Chart(ctxpo).PolarArea(pdata);
      
      var ctxp = $("#pieChartDemo").get(0).getContext("2d");
      var barChart = new Chart(ctxp).Pie(pdata);

      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Task', 'Hours per Day'],
          ['Work',     17],
          ['Watch TV', 2],
          ['Sleep',    7]
        ]);
        var options = {
          title: 'My Daily Activities',
          is3D: true,
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));
        chart.draw(data, options);
      }
    </script>
    <script>
  var map;
  function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: -6.21462, lng: 106.84513},
    zoom: 11
   });
     /*var arrdt = {"data":[{
    "lat":"-6.180944",
    "lang":"106.829002",
    "lokasi" : "Jakarta Pusat Balaikota"
  },{
    "lat":"-6.218365",
    "lang":"106.754334",
     "lokasi" : "Jakarta Barat Joglo" 
  },{
    "lat":"-6.213842",
    "lang":"106.906612",
     "lokasi" : "Jakarta Timur Mampang" 
  },{
    "lat":"-6.258592",
    "lang":"106.828340",
     "lokasi" : "Jakarta Selatan Mampang" 
  }],"status_code":"000","status":"Success"};
*/
var arrdt = [
        ['Jakarta Pusat Balaikota', -6.180944, 106.829002, 4],
        ['akarta Barat Joglo', -6.218365, 106.754334, 1],
        ['Jakarta Timur Mampang', -6.213842, 106.906612, 3],
        ['Jakarta Selatan Mampang', -6.258592, 106.828340, 2]
      ];

    for(var i=0;i<arrdt.length;i++) {
      var arr = arrdt[i];
      //console.log(arr.lat);
      //Marker(parseInt(arr.lat), parseInt(arr.lng), map, arr.lokasi, i);
              //addIndividualLabel(markersUserArray[i], opzd.nama);
      var image = '<?php echo base_url();?>assets/images/icons/marker.png';
      var marker = new google.maps.Marker({
          position: {lat: arr[1], lng: arr[2]},
          map: map,
          icon:image,
          title: arr[0]
        });
    }


  }

</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDBizWzEFNqFreOUnz2h-YhH8eNgVHyfQg&callback=initMap"
    async defer></script>
  </body>
</html>