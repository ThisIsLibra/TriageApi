/*
 * Copyright (C) 2023 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package triageapi.model;

/**
 * The selected Triage cloud environment.<br>
 * <br>
 *
 * @see #PUBLIC the public cloud environment<br>
 * @see #PRIVATE the private cloud environment<br>
 * @see #RECORDED_FUTURE the Recorded Future cloud environment<br>
 *
 * @author Max 'Libra' Kersten
 */
public enum TriageEnvironment {
    PUBLIC,
    PRIVATE,
    RECORDED_FUTURE,
    RECORDED_FUTURE_US
}
