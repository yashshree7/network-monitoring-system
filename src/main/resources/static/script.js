const API_BASE_URL = "/api";

let selectedHostId = null;


// Load Dashboard Overview
async function loadDashboardSummary() {

    try {

        const response = await fetch(
            `${API_BASE_URL}/dashboard/summary`
        );

        const summary = await response.json();

        document.getElementById("totalHosts").textContent =
            summary.totalHosts;

        document.getElementById("upHosts").textContent =
            summary.upHosts;

        document.getElementById("downHosts").textContent =
            summary.downHosts;

        document.getElementById("overallAvailability").textContent =
            `${summary.overallAvailability.toFixed(2)}%`;

    } catch (error) {

        console.error(
            "Error loading dashboard summary:",
            error
        );

    }
}


// Load all monitored hosts
async function loadHosts() {

    try {

        const response = await fetch(
            `${API_BASE_URL}/hosts`
        );

        const hosts = await response.json();

        const tableBody =
            document.getElementById("hostTableBody");

        tableBody.innerHTML = "";

        hosts.forEach(host => {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${host.id}</td>

                <td>${host.hostName}</td>

                <td>${host.hostAddress}</td>

                <td id="status-${host.id}" class="status-not-checked">
                    Not Checked
                </td>

                <td id="response-${host.id}">
                    -
                </td>

                <td>
                    <button onclick="checkHost(${host.id})">
                        Check Host
                    </button>
                </td>
            `;

            tableBody.appendChild(row);

        });

    } catch (error) {

        console.error(
            "Error loading hosts:",
            error
        );

    }
}


// Check a host
async function checkHost(hostId) {

    selectedHostId = hostId;

    try {

        const response = await fetch(
            `${API_BASE_URL}/monitoring/check/${hostId}`,
            {
                method: "POST"
            }
        );

        const result = await response.json();

        const statusElement =
            document.getElementById(
                `status-${hostId}`
            );

        const responseElement =
            document.getElementById(
                `response-${hostId}`
            );


        // Update host status
        statusElement.textContent =
            result.status;


        // Update response time
        responseElement.textContent =
            result.responseTime !== null
                ? `${result.responseTime} ms`
                : "-";


        // Apply status styling
        if (result.status === "UP") {

            statusElement.className =
                "status-up";

        } else {

            statusElement.className =
                "status-down";

        }


        // Refresh selected host information

        await loadKpi(hostId);

        await loadHistory(hostId);

        await loadIncidents(hostId);


        // Refresh dashboard overview

        await loadDashboardSummary();

    } catch (error) {

        console.error(
            "Error checking host:",
            error
        );

    }
}


// Load KPI information
async function loadKpi(hostId) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/monitoring/kpi/${hostId}`
        );

        const kpi = await response.json();


        document.getElementById(
            "totalChecks"
        ).textContent =
            kpi.totalChecks;


        document.getElementById(
            "availability"
        ).textContent =
            `${kpi.availabilityPercentage.toFixed(2)}%`;


        document.getElementById(
            "failures"
        ).textContent =
            kpi.failureCount;


        document.getElementById(
            "avgResponse"
        ).textContent =
            `${kpi.averageResponseTime.toFixed(2)} ms`;

    } catch (error) {

        console.error(
            "Error loading KPI:",
            error
        );

    }
}


// Load monitoring history
async function loadHistory(hostId) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/monitoring/history/${hostId}`
        );

        const history = await response.json();

        const tableBody =
            document.getElementById(
                "historyTableBody"
            );

        tableBody.innerHTML = "";


        history.forEach(result => {

            const row =
                document.createElement("tr");


            row.innerHTML = `
                <td>${result.status}</td>

                <td>
                    ${
                result.responseTime !== null
                    ? result.responseTime + " ms"
                    : "-"
            }
                </td>

                <td>
                    ${result.errorMessage || "-"}
                </td>

                <td>
                    ${result.checkedAt}
                </td>
            `;


            tableBody.appendChild(row);

        });

    } catch (error) {

        console.error(
            "Error loading history:",
            error
        );

    }
}


// Load network incidents
async function loadIncidents(hostId) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/incidents/${hostId}`
        );

        const incidents = await response.json();

        const tableBody =
            document.getElementById(
                "incidentTableBody"
            );

        tableBody.innerHTML = "";


        incidents.forEach(incident => {

            const row =
                document.createElement("tr");


            row.innerHTML = `
                <td>${incident.incidentType}</td>

                <td>
                    ${incident.description}
                </td>

                <td>
                    ${incident.status}
                </td>

                <td>
                    ${incident.createdAt}
                </td>
            `;


            tableBody.appendChild(row);

        });

    } catch (error) {

        console.error(
            "Error loading incidents:",
            error
        );

    }
}


// Load dashboard when page opens
window.addEventListener(
    "DOMContentLoaded",
    () => {

        loadDashboardSummary();

        loadHosts();

    }
);