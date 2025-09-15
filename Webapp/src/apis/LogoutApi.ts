import { request } from './axios';
import type { Router } from 'vue-router';

// const getCookie = (key: string): string | null => {
//   const cookieArr = document.cookie.split('; ');
//   for (const cookie of cookieArr) {
//     const [name, value] = cookie.split('=');
//     if (name === key) {
//       return decodeURIComponent(value);
//     }
//   }
//   return null;
// };

export const casLogout = async (router: Router) => {
  try {

    await request('/api/logout', {}, 'get'); 

  } catch (error) {
    console.error('登出请求失败:', error);

    await router.push('/home');
    throw error;
  }
};