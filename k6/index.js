import http from 'k6/http';
import { Rate } from 'k6/metrics';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/2.4.0/dist/bundle.js';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const JSON_HEADERS = { headers: { 'Content-Type': 'application/json' } };

const failureRate = new Rate('failed_requests');
const useCases = JSON.parse(open('./tests/usecases.json'));
const dataToCreate = JSON.parse(open('./tests/dataToCreate.json'));

// Jeu de données initial pour que la récupération de la liste ne soit jamais vide.
export function setup() {
    console.log('Setup: insertion du jeu de données initial');
    dataToCreate.forEach((book) =>
        http.post(`${BASE_URL}/books`, JSON.stringify(book), JSON_HEADERS),
    );
}

// Exécute aléatoirement la création d'un livre ou la récupération de la liste.
export function test_api_endpoints_config() {
    const useCase = useCases[Math.floor(Math.random() * useCases.length)];

    if (useCase.type === 'GET') {
        const res = http.get(`${BASE_URL}/books`);
        failureRate.add(res.status !== 200);
    } else {
        const body = JSON.stringify({ title: useCase.title, author: useCase.author });
        const res = http.post(`${BASE_URL}/books`, body, JSON_HEADERS);
        failureRate.add(res.status !== 201);
    }
}

export function handleSummary(data) {
    return {
        'summary.html': htmlReport(data),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}
