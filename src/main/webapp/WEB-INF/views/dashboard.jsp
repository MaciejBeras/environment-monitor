<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Environmental Monitoring</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom@2.0.1/dist/chartjs-plugin-zoom.min.js"></script>
    <style>
      :root{
        --bg:#0f1115;
        --panel:#161a22;
        --text:#e7eaf0;
        --muted:#aeb4c0;
        --grid:#2a3240;
        --accent:#3a7afe;
        --btn-bg:#222836;
        --btn-bg-h:#2b3344;
        --btn-text:#e7eaf0;
        --line-temp:#ff6b6b;   /* czerwony jaśniejszy */
        --line-hum:#4da3ff;    /* niebieski jaśniejszy */
        --line-press:#32d296;  /* zielony jaśniejszy */
      }

      /* Dark theme base */
      html, body {
        height: 100%;
        background: var(--bg);
        color: var(--text);
        font-family: Arial, sans-serif;
        margin: 0;
      }

      body { padding: 20px; text-align: center; }

      h1 { margin: 0 0 10px; color: var(--text); }

      .current-values {
        font-size: 1.2em;
        margin: 0 auto 20px;
        color: var(--text);
      }

      #chart-container {
        width: 90%;
        max-width: 900px;
        margin: 0 auto;
        height: 420px;
        background: var(--panel);
        border-radius: 12px;
        box-shadow: 0 6px 18px rgba(0,0,0,.35) inset, 0 2px 12px rgba(0,0,0,.2);
        padding: 12px;
      }

      .controls { margin-top: 12px; }

      .controls button {
        padding: 8px 14px;
        margin: 0 6px;
        cursor: pointer;
        background: var(--btn-bg);
        color: var(--btn-text);
        border: 1px solid var(--grid);
        border-radius: 8px;
        transition: background .15s ease, transform .02s linear;
      }
      .controls button:hover { background: var(--btn-bg-h); }
      .controls button:active { transform: translateY(1px); }

      /* Ulepszenia dla emoji/tekstu pomocniczego */
      .current-values span { color: var(--accent); }
    </style>
</head>
<body>
<h1>Environment Monitor</h1>

<div class="current-values">
    🌡 Temperature: <span id="temp">--</span> °C &nbsp;|&nbsp;
    💧 Humidity: <span id="hum">--</span> % &nbsp;|&nbsp;
    🌬 Pressure: <span id="press">--</span> hPa
</div>

<div id="chart-container">
    <canvas id="envChart"></canvas>
</div>

<div class="controls">
    <button onclick="chart.resetZoom()">Reset zoom</button>
    <button onclick="downloadData()">📥 Pobierz dane (XML)</button>
</div>

<script>
  function downloadData() {
    window.location.href = "http://localhost:8080/export/xml";
  }
</script>


<script>
  let chart;

  async function loadData() {
    const response = await fetch('/api/readings/last6h');
    const data = await response.json();

    if (data.length === 0) return;

    data.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

    const labels = data.map(item =>
        new Date(item.timestamp + 'Z').toLocaleString('pl-PL', {
          timeZone: 'Europe/Warsaw',
          hour: '2-digit',
          minute: '2-digit'
        })
    );

    const temps = data.map(item => item.temperature);
    const hums  = data.map(item => item.humidity);
    const press = data.map(item => item.pressure);

    const last = data[data.length - 1];
    document.getElementById('temp').textContent  = last.temperature.toFixed(1);
    document.getElementById('hum').textContent   = last.humidity.toFixed(1);
    document.getElementById('press').textContent = last.pressure.toFixed(1);

    if (!chart) {
      const ctx = document.getElementById('envChart').getContext('2d');

      /* ciemne tło samego canvasa */
      ctx.canvas.style.backgroundColor = getComputedStyle(document.documentElement)
      .getPropertyValue('--panel').trim();

      chart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            { label: 'Temperature (°C)', data: temps, borderColor: getCss('--line-temp'), fill: false, yAxisID: 'y', tension: 0.25 },
            { label: 'Humidity (%)',    data: hums,  borderColor: getCss('--line-hum'),  fill: false, yAxisID: 'y1', tension: 0.25 },
            { label: 'Pressure (hPa)',  data: press, borderColor: getCss('--line-press'),fill: false, yAxisID: 'y2', tension: 0.25 }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          interaction: { mode: 'index', intersect: false },
          scales: {
            x: {
              ticks: { color: getCss('--text') },
              grid: { color: getCss('--grid') }
            },
            y: {
              type: 'linear',
              position: 'left',
              title: { display: true, text: 'Temp (°C)', color: getCss('--muted') },
              ticks: { color: getCss('--text') },
              grid: { color: getCss('--grid') }
            },
            y1: {
              type: 'linear',
              position: 'right',
              title: { display: true, text: 'Humidity (%)', color: getCss('--muted') },
              ticks: { color: getCss('--text') },
              grid: { drawOnChartArea: false, color: getCss('--grid') }
            },
            y2: {
              type: 'linear',
              position: 'right',
              title: { display: true, text: 'Pressure (hPa)', color: getCss('--muted') },
              ticks: { color: getCss('--text') },
              grid: { drawOnChartArea: false, color: getCss('--grid') }
            }
          },
          plugins: {
            legend: {
              labels: { color: getCss('--text') }
            },
            tooltip: {
              titleColor: getCss('--text'),
              bodyColor: getCss('--text'),
              backgroundColor: 'rgba(22,26,34,0.9)',
              borderColor: getCss('--grid'),
              borderWidth: 1
            },
            zoom: {
              zoom: {
                wheel: { enabled: true },
                pinch: { enabled: true },
                mode: 'x'
              },
              pan: {
                enabled: true,
                mode: 'x'
              }
            }
          }
        }
      });
    } else {
      chart.data.labels = labels;
      chart.data.datasets[0].data = temps;
      chart.data.datasets[1].data = hums;
      chart.data.datasets[2].data = press;
      chart.update();
    }
  }

  /* helper: pobierz wartość CSS variable jako string */
  function getCss(varName){ return getComputedStyle(document.documentElement).getPropertyValue(varName).trim(); }

  loadData();
  setInterval(loadData, 10000);
</script>
</body>
</html>
