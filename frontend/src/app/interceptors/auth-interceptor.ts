import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('jwt_token');

  const router = inject(Router);

  let requestToForward = req;

  if (token) {
    requestToForward = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  return next(requestToForward).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 || error.status === 403) {
        console.warn('Token expired or invalid. Redirecting to login...');
        localStorage.removeItem('jwt_token');
        router.navigate(['/login']);
      }

      return throwError(() => error);
    }),
  );
};
