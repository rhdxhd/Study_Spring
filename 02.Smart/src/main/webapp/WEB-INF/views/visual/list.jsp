<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  




<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
#legend span { width: 44px; height:17px; margin-right: 5px }
#legend li { display: flex; align-items: center; }
</style>
</head>
<body>
<h3 class="mb-4">사원정보분석</h3>

<!-- 부서별 사원수 -->
<!-- 채용인원수 -->
<ul class="nav nav-tabs">
  <li class="nav-item">
    <a class="nav-link active">부서원수</a>
  </li>
  <li class="nav-item">
    <a class="nav-link">채용인원수</a>
  </li>
  <li class="nav-item">
    <a class="nav-link">Link</a>
  </li>
</ul>


<div id="tab-content" class="py-3" style="height:520px">
	<canvas id="chart" class="m-auto" style="height:100%"></canvas>
</div>







<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-autocolors"></script>
<script>

function makeLegend() {
	var li = "";
	var i;
	for(var i=0; i<=5; i++) {
		li += `<li class="col-auto"><span></span><font>\${i*10}명~\${i*10+9}명</font></li>`;
	}
	li += `<li class="col-auto"><span></span><font>\${i*10}명이상</font></li>`;
	
	var tag = `
		<ul class="row d-flex justify-content-center" id="legend">
			\${li}
		</ul>
	`;
	$("#tab-content").after( tag );
	$("#legend span").after( function () {
		$(this).css("background-color", colors[idx]);
	})
}




/* $("ul.nav-tabs li").on("click", function() {
	$("ul.nav-tabs li a").removeClass("active");
	$(this).children("a").addClass("active");
})

$("ul.nav-tabs li").on("mouseover", function() {
	$(this).removeClass("shadow");
})

$("ul.nav-tabs li").on("mouseleave", function() {
	$(this).removeClass("shadow");
}) */


$("ul.nav-tabs li").on({
	"click": function() {
		$("ul.nav-tabs li a").removeClass("active") ;
		$(this).children("a").addClass("active") ;
		
		
		var idx = $(this).index();
		if (idx==0)			department();
		else if (idx==1) 	hirement();
		
	},
	"mouseover": function() {
		$(this).addClass("shadow") ;
	},
	"mouseleave": function() {
		$(this).removeClass("shadow") ;
	}	
})



//부서원수
function department() {
	//sampleChart();
	
	$.ajax({
		url: "department"
	}).done(function ( response ) {
		console.log( response )
		
		var info = {};
		info.category = [], info.datas = [], info.colors = [];
		
		$(response).each( function() {
			info.datas.push( this.COUNT ); //차트표현할 데이터
			info.category.push( this.DEPARTMENT_NAME ); //x축 범주
			//사원수에 따라 색상값 배열 만들기
			info.colors.push( colors[ Math.floor( this.COUNT/10 ) ] )
		})
		
		//barChart( info );
		lineChart( info );
	})
}




/*
색상 적용
10인 미만(0~9):0 첫번째 색, 20인(10~19):1 미만 두번째 색(20~29):2 ...
19/10=1.9 --> 1
*/


Chart.defaults.font.size = 16;
Chart.defaults.plugins.legend.position = "bottom";
Chart.register(ChartDataLabels);// 차트에 데이터수치를 표시하기 
Chart.defaults.set('plugins.datalabels', {
	  color: '#000',
	  anchor: 'end',
	  font: { weight: "bold" },
});
Chart.register( window['chartjs-plugin-autocolors']); //자동색상적용

var colors = [ "#6799FF", "#4374D9", "#AFE0FF", "#58919A", "#B7F0B1",
				"#86E57F", "#1A7913", "#53C14B", "#1DDB16", "#CEFBC9", "#FAED7D",];
				
				


function barChart( info ) {
	console.log( info )
	
	new Chart( $("#chart"), {
		type: "bar",
		data: {
			labels: info.category,
   	        datasets: [{
		        label: '부서원수',
		        data: info.datas,
		        borderWidth: 1,
		        barPercentage: 0.5,
		        backgroundColor: info.colors,
		      }]
		},
		  options: {
			  responsive: false,
			  maintainAspectRatio: false,
			  plugins: {
			      datalabels: {
			        formatter: function(value, context) {
			          return value + "명";
			        }
			      },
			      legend: { display: false },
			      autocolors: {
			          mode: 'data'
			        },
			    },
		      scales: {
		        y: {
		          beginAtZero: true,
		          title: {
		              color: 'red',
		              display: true,
		              text: '부서별 사원수'
		            }
		        }
		    },
		}
		
	})
	
	makeLegend();
	
}





function lineChart( info ) {
	console.log( info )
	
	new Chart( $("#chart"), {
		type: "line",
		data: {
			labels: info.category,
   	        datasets: [{
		        label: '부서원수',
		        data: info.datas,
		        borderColor: "#0000ff",
		        pointRadius: 5,
		        tension: 0.1,
		        backgroundColor: "#ff0000",
		      }]
		},
		  options: {
			  responsive: false,
			  maintainAspectRatio: false,
			  plugins: {
			      datalabels: {
			        formatter: function(value, context) {
			          return value + "명";
			        }
			      },
			      legend: { display: false },
			    },
		      scales: {
		        y: {
		          beginAtZero: true,
		          title: {
		              color: 'red',
		              display: true,
		              text: '부서별 사원수'
		            }
		        }
		    },
		}
		
	})
	
	makeLegend();
	
}


















function sampleChart() {

  //const ctx = document.getElementById('chart');

  new Chart( $("#chart") , {
    type: 'bar',
    data: {
      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
      datasets: [{
        label: '# of Votes',
        data: [12, 19, 3, 5, 2, 3],
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}
  










//채용인원수
function hirement() {
	

	
}








$(function() {
	$("ul.nav-tabs li").eq(0).trigger("click"); //부서원수 탭 강제 클릭
})








</script>



</body>
</html>