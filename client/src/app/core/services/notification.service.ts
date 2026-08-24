import { Injectable, signal } from '@angular/core';

export interface ToastMessage {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private nextId = 1;
  private toastsSignal = signal<ToastMessage[]>([]);

  toasts = this.toastsSignal.asReadonly();

  showError(message: string): void {
    this.addToast(message, 'error');
  }

  showSuccess(message: string): void {
    this.addToast(message, 'success');
  }

  showInfo(message: string): void {
    this.addToast(message, 'info');
  }

  showWarning(message: string): void {
    this.addToast(message, 'warning');
  }

  private addToast(message: string, type: 'success' | 'error' | 'info' | 'warning'): void {
    const id = this.nextId++;
    const toast = { id, message, type };
    this.toastsSignal.update(toasts => [...toasts, toast]);

    setTimeout(() => {
      this.removeToast(id);
    }, 5000); // 5 seconds duration
  }

  removeToast(id: number): void {
    this.toastsSignal.update(toasts => toasts.filter(t => t.id !== id));
  }
}
