// https://tc39.es/ecma402/#sec-datetimeformat-abstracts
export const dateFormatOptions = {
  weekday: undefined,
  year: "2-digit",
  month: "short",
  day: "numeric",
  hour: "numeric",
  minute: "numeric",
}

export function formatDate(date, locale) {
  return date.toLocaleString(locale, dateFormatOptions);
}

export function formatAllDates(dateAttribute, locale, root = document) {
  root.querySelectorAll(`[${dateAttribute}]`).forEach(dateElement => {
    const date = new Date(dateElement.getAttribute(dateAttribute));
    dateElement.innerText = formatDate(date, locale);
  });
}
