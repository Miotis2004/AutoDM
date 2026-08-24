import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';
import { ApiErrorResponse } from '../models/api-error.model';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'An unexpected error occurred.';

      if (error.status === 0) {
        // A client-side or network error occurred.
        errorMessage = 'Server unavailable. Please check your connection and try again.';
      } else {
        // The backend returned an unsuccessful response code.
        const apiError = error.error as ApiErrorResponse;

        if (apiError && apiError.message) {
            errorMessage = apiError.message;
            if (apiError.validationErrors) {
               const errors = Object.values(apiError.validationErrors).join(', ');
               errorMessage += `: ${errors}`;
            }
        } else {
            errorMessage = `Backend returned code ${error.status}: ${error.statusText}`;
        }
      }

      notificationService.showError(errorMessage);
      return throwError(() => error);
    })
  );
};
