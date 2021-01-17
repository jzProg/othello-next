import axios from 'axios';

export default function useApiHelper() {

  const API_ROOT = '/api/game';

  const endpoints = {
    INIT: '/startNewGame',
    CHOOSE: '/choose',
    PLAY: '/play',
    AI: '/getAIMove',
  };

  function sendMove(move, successCallback, errorCallback = () => {}, params = {}) {
    axios.get(API_ROOT + endpoints[move], { params })
          .then(response => successCallback(response))
          .catch(error => errorCallback(error));
  }

  return {
    sendMove
  }
}
