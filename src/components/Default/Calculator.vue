<!--Calculator.vue-->

<template>
    <div class="card">
        <h2 class="text">Choose matrix size</h2>
        <InputNumber v-model="matrixSize" showButtons buttonLayout="horizontal" style="width: 3rem" :min="1" max="10">
            <template #incrementbuttonicon>
                <span class="pi pi-plus" />
            </template>
            <template #decrementbuttonicon>
                <span class="pi pi-minus" />
            </template>
        </InputNumber>

        <div class="matrix-container">
            <div v-for="(row, rowIndex) in matrix" :key="rowIndex" class="matrix-row">
                <input v-for="(value, colIndex) in row.slice(0, matrixSize)" :key="colIndex"
                    v-model="matrix[rowIndex][colIndex]" class="matrix-input" />
                <div class="separator"></div>
                <input v-model="vector[rowIndex]" class="matrix-input vector-input" placeholder="B" />
            </div>
        </div>

        <Button type="button" label="Calculate" @click="addTask" class="dialog-button"></Button>
        <Button type="button" label="Generate" @click="generateMatrix" class="generate-button"></Button>

        <!-- Tabs Section -->
        <Tabs value="0">
            <TabList>
                <Tab value="0">Current Result</Tab>
                <Tab value="2">History</Tab>
            </TabList>
            <TabPanels>
                <!-- Current Result Tab -->
                <TabPanel value="0">
                    <ul>
                        <li v-for="task in tasks" :key="task.id" class="history-item">
                            <div class="history-header">
                                <span>Task ID: {{ task.id }} - {{ task.status === 'calculating' ? 'Calculating...' :
                                    'Completed'
                                    }}</span>
                                <button @click="toggleDetails(task.id)">
                                    <i :class="expandedHistory === task.id ? 'pi pi-angle-up' : 'pi pi-angle-down'"></i>
                                </button>
                            </div>
                            <div v-if="expandedHistory === task.id" class="history-details">
                                <div class="matrix-container">
                                    <div v-for="(row, rowIndex) in task.data.A" :key="'row-' + rowIndex"
                                        class="matrix-row">
                                        <input v-for="(value, colIndex) in row" :key="'col-' + colIndex"
                                            v-model="row[colIndex]" class="matrix-input" :readonly="true" />
                                        <div class="separator"></div>
                                        <input v-model="task.data.B[rowIndex]" class="matrix-input vector-input"
                                            :readonly="true" />
                                    </div>
                                </div>

                                <h3>Results:</h3>
                                <div class="task-results">
                                    <p v-for="(result, index) in task.results" :key="index"
                                        style="white-space: pre-wrap;">
                                        {{ result }}
                                    </p>
                                </div>
                            </div>
                        </li>
                    </ul>
                </TabPanel>
                <!-- History Tab -->
                <TabPanel value="2">
                    <ul>
                        <li v-for="history in userHistory" :key="history.id" class="history-item">
                            <div class="history-header">
                                <span>History ID: {{ history.id }}</span>
                                <button @click="toggleDetails(history.id)">
                                    <i
                                        :class="expandedHistory === history.id ? 'pi pi-angle-up' : 'pi pi-angle-down'"></i>
                                </button>
                            </div>
                            <div v-if="expandedHistory === history.id" class="history-details">
                                <div class="matrix-container">
                                    <div v-for="(row, rowIndex) in history.data.A" :key="'row-' + rowIndex"
                                        class="matrix-row">
                                        <input v-for="(value, colIndex) in row" :key="'col-' + colIndex"
                                            v-model="row[colIndex]" class="matrix-input" :readonly="true" />
                                        <div class="separator"></div>
                                        <input v-model="history.data.B[rowIndex]" class="matrix-input vector-input"
                                            :readonly="true" />
                                    </div>
                                </div>

                                <h3>Значення обчислень:</h3>
                                <ul>
                                    <p class="m-0" style="white-space: pre-wrap;">
                                        {{ formattedDataHistory }}
                                    </p>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </TabPanel>
            </TabPanels>
        </Tabs>
    </div>
</template>


<script setup>
import { ref, watch, onMounted } from 'vue';
import 'primeicons/primeicons.css';
import JacobiService from '@/services/jacobiService';
import SaveHistoryService from '@/services/saveHistoryService';
import HistoryService from '@/services/historyService';
import '@/container/userstore.js';

const matrixSize = ref(3);
const matrix = ref([]);
const vector = ref([]);
const errorMessage = ref('');
const formattedData = ref('');
const formattedDataHistory = ref('');
const expandedHistory = ref(null);
const tasks = ref([]);

const initialTasks = () => [];
tasks.value = initialTasks();

const MAX_ACTIVE_TASKS = 3;

function addTask() {
    const activeTasks = tasks.value.filter(task => task.status === 'calculating').length;
    if (activeTasks >= MAX_ACTIVE_TASKS) {
        return;
    }

    const task = {
        id: tasks.value.length + 1,
        status: 'calculating',
        data: {
            A: matrix.value.slice(0, matrixSize.value),
            B: vector.value.slice(0, matrixSize.value),
        },
        results: [],
    };

    tasks.value.push(task);
    calculateTask(task);
    console.log("Task added:", task);
}

function addResultToTask(taskId, result) {
    const task = tasks.value.find((t) => t.id === taskId);
    if (task) {
        task.results.push(result);
        console.log("Task after adding result:", task);
    } else {
        console.error(`Task with id ${taskId} not found.`);
    }
}

async function calculateTask(task) {
    try {
        const response = await JacobiService.calculateJacobi(task.data.A, task.data.B);
        const formattedOutput = formatJacobiResponse(response);
        task.status = 'completed';
        addResultToTask(task.id, formattedOutput);

        const result = [];
        result.value = response;

        const history = {
            data: task.data,
            result: result.value
        };
        await SaveHistoryService.saveHistory(history);
        formattedData.value = formattedOutput;
        tasks.value = [...tasks.value];
    } catch (error) {
        task.status = 'error';
        errorMessage.value = 'Error during calculation';
        console.error(error);
    }
}

//function for formatting data
function formatJacobiResponse1(response) {
    if (!response.result) {
        console.error("No 'result' found in the response");
        return '';
    }

    const formattedResults = response.result.map((result) => {
        let formattedValues = result.values
            .map((value, index) => `x${index + 1} = ${value.toFixed(6)}`)
            .join('\n');
        return `iteration: ${result.iteration}\n${formattedValues}`;
    });
    return formattedResults.join('\n\n');
}

function formatJacobiResponse(response) {
    const formattedResults = response.map((iterationResult, index) => {
        const values = iterationResult.values
            .map((value, i) => `x${i + 1} = ${value.toFixed(6)}`)
            .join('\n');

        return `Iteration ${index + 1}:\n ${values}`;
    });

    return formattedResults.join('\n\n');
}

//functions for history
const userHistory = ref([]);

function toggleDetails(id) {
    expandedHistory.value = expandedHistory.value === id ? null : id;
}

const fetchHistories = async () => {
    let token = localStorage.getItem('token');
    if (token) {
        try {
            const response = await HistoryService.getUserHistories();
            console.log('Fetched histories:', response.data);

            if (response.data && Array.isArray(response.data) && response.data.length > 0) {
                userHistory.value = response.data;

                const formattedResults = formatJacobiResponse1(response.data[0]);
                formattedDataHistory.value = formattedResults;
                console.log('Formatted results:', formattedResults);
            } else {
                console.error('No histories found or invalid response structure.');
            }
        } catch (error) {
            console.error('Error fetching histories:', error);
        }
    } else {
        console.log('You must be authorized to view the history.');
    }
};

onMounted(fetchHistories);

//functions for matrix
function getRandomNumber(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function generateRandMatrix() {
    let matrixRand = [];
    for (let i = 0; i < matrixSize.value; i++) {
        let sum = 0;
        matrixRand[i] = [];
        for (let j = 0; j < matrixSize.value; j++) {
            if (i !== j) {
                matrixRand[i][j] = getRandomNumber(1, 100);
                sum += Math.abs(matrixRand[i][j]);
            }
            matrixRand[i][i] = sum + getRandomNumber(1, 100) + 1;
        }
    }
    return matrixRand;
}

function generateRandVector() {
    let vectorRand = [];
    for (let i = 0; i < matrixSize.value; i++) {
        vectorRand[i] = getRandomNumber(1, 100);
    }
    return vectorRand;
}

function updateMatrix() {
    const newMatrix = [];
    const newVector = [];

    for (let i = 0; i < matrixSize.value; i++) {
        const row = matrix.value[i]
            ? [...matrix.value[i].slice(0, matrixSize.value)]
            : Array(matrixSize.value).fill(null);
        while (row.length < matrixSize.value) {
            row.push(null);
        }
        newMatrix.push(row);

        newVector.push(vector.value[i] !== undefined ? vector.value[i] : null);
    }

    matrix.value = newMatrix;
    vector.value = newVector;
}
function generateMatrix() {
    matrix.value = generateRandMatrix();
    vector.value = generateRandVector();
}

watch(matrixSize, updateMatrix);
updateMatrix();
</script>

<style scoped>
.card {
    padding: 1rem;
    border-radius: 16px;
    background-color: #f3f5f8;
}

.text {
    margin: 1rem 0;
    font-family: 'Roboto', sans-serif;
}

.matrix-container {
    margin-top: 1rem;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.matrix-row {
    display: flex;
    margin-bottom: 0.5rem;
}

.matrix-input {
    width: 3rem;
    margin-right: 0.5rem;
    text-align: center;
}

.vector-input {
    margin-left: 1rem;
}

.separator {
    width: 2px;
    height: 2rem;
    background-color: #000;
    margin-left: 0.5rem;
    margin-right: 0.5rem;
}

.dialog-button {
    font-family: 'Roboto', sans-serif;
    background-color: #EB5436;
    padding: 0.5rem 1rem;
    border: none;
    color: white;
    cursor: pointer;
    border-radius: 16px;
}

.generate-button {
    font-family: 'Roboto', sans-serif;
    padding: 0.5rem 1rem;
    border: none;
    color: white;
    cursor: pointer;
    border-radius: 16px;
}

.results-panel {
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #f9f9f9;
    margin: 20px 0;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.result-content {
    white-space: pre-wrap;
    font-family: 'Roboto', sans-serif;
    font-size: 14px;
}

.error-message {
    color: red;
    font-weight: bold;
    margin-top: 10px;
    font-family: 'Roboto', sans-serif;
}

.history-item {
    margin: 10px 0;
    border: 1px solid #ddd;
    padding: 10px;
    border-radius: 5px;
}

.history-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.history-details {
    margin-top: 10px;
    background-color: #f9f9f9;
    padding: 10px;
    border-radius: 5px;
}

pre {
    white-space: pre-wrap;
    font-family: monospace;
}
</style>
